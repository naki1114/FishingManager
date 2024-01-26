package com.example.fishingmanager.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.icu.util.LocaleData
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.adapter.ConditionCombineAdapter
import com.example.fishingmanager.adapter.ConditionSearchLocationAdapter
import com.example.fishingmanager.adapter.ConditionSelectFishAdapter
import com.example.fishingmanager.adapter.ConditionTideAdapter
import com.example.fishingmanager.adapter.ConditionWeatherAdapter
import com.example.fishingmanager.data.SearchLocation
import com.example.fishingmanager.databinding.FragmentConditionBinding
import com.example.fishingmanager.viewModel.ConditionViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.lang.StringBuilder
import java.util.Calendar
import java.util.Locale
import javax.mail.Quota.Resource
import kotlin.concurrent.thread


class ConditionFragment : Fragment() {

    val TAG = "ConditionFragment"

    lateinit var binding: FragmentConditionBinding

    lateinit var viewModel: ConditionViewModel

    lateinit var weatherAdapter: ConditionWeatherAdapter
    lateinit var combineAdapter: ConditionCombineAdapter
    lateinit var tideAdapter: ConditionTideAdapter
    lateinit var selectFishAdapter: ConditionSelectFishAdapter
    lateinit var searchLocationAdapter: ConditionSearchLocationAdapter

    lateinit var conditionSharedPreferences: SharedPreferences
    lateinit var conditionEditor: SharedPreferences.Editor
    lateinit var searchLocation: SearchLocation

    lateinit var loadingAnimationRight: Animation
    lateinit var loadingAnimationLeft: Animation
    var loadingAnimationStatus = false
    var previousLayout = ""

    lateinit var animationThread: Thread


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_condition, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        setView()
        observeLiveData()

    } // onViewCreated()


    fun setVariable() {

        checkedLocationSharedPreference()

        viewModel = ConditionViewModel(
            (activity as MainActivity).getWeatherList(),
            (activity as MainActivity).getTideList(),
            (activity as MainActivity).getIndexList(),
            searchLocation
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        weatherAdapter = ConditionWeatherAdapter()
        combineAdapter = ConditionCombineAdapter()
        tideAdapter = ConditionTideAdapter()
        selectFishAdapter =
            ConditionSelectFishAdapter(ConditionSelectFishAdapter.ItemClickListener {
                viewModel.changeFish(it.fishName)
            })
        searchLocationAdapter =
            ConditionSearchLocationAdapter(ConditionSearchLocationAdapter.ItemClickListener {
                viewModel.changeLocation(it)
            })

        binding.conditionCombineRecyclerView.adapter = combineAdapter
        binding.conditionWeatherRecyclerView.adapter = weatherAdapter
        binding.conditionTideRecyclerView.adapter = tideAdapter
        binding.conditionSelectFishRecyclerView.adapter = selectFishAdapter
        binding.conditionSearchLocationRecyclerView.adapter = searchLocationAdapter

        loadingAnimationRight =
            AnimationUtils.loadAnimation(activity, R.anim.loading_animation_right)
        loadingAnimationLeft = AnimationUtils.loadAnimation(activity, R.anim.loading_animation_left)

    } // setVariable()


    fun setView() {

        binding.conditionSelectDateCalendarView.setTitleFormatter(
            MonthArrayTitleFormatter(
                resources.getTextArray(
                    R.array.custom_months
                )
            )
        )
        binding.conditionSelectDateCalendarView.setWeekDayFormatter(
            ArrayWeekDayFormatter(
                resources.getTextArray(
                    R.array.custom_weekdays
                )
            )
        )

        val calendar : Calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 2)
        calendar.add(Calendar.MONTH, 1)

        val minDay = CalendarDay.today()
        val maxDay = CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))

        binding.conditionSelectDateCalendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader)
        binding.conditionSelectDateCalendarView.addDecorators(
            TodayDecorator(requireActivity()),
            SundayDecorator(),
            SaturdayDecorator(),
            MinMaxDecorator(maxDay, minDay)
        )
        binding.conditionSelectDateCalendarView.selectedDate = CalendarDay.today()
        binding.conditionSelectDateCalendarView.setTitleFormatter(object : TitleFormatter {
            override fun format(day: CalendarDay?): CharSequence {

                val inputText: org.threeten.bp.LocalDate = day!!.date
                val calendarHeaderElements = inputText.toString().split("-")
                val calendarHeaderBuilder = StringBuilder()
                calendarHeaderBuilder.append(calendarHeaderElements[0])
                    .append("   ")
                    .append(calendarHeaderElements[1])

                return calendarHeaderBuilder.toString()

            }

        })

        binding.conditionSelectDateChoiceButton.setOnClickListener {

            viewModel.changeDate(binding.conditionSelectDateCalendarView.selectedDate!!.date.toString())

        }

    } // setView()


    fun observeLiveData() {

        viewModel.liveDataCurrentLayout.observe(viewLifecycleOwner, Observer {

            changeLayout(it)

        })

        viewModel.liveDataCombineList.observe(viewLifecycleOwner, Observer {

            combineAdapter.setItem(it)

        })

        viewModel.liveDataWeatherList.observe(viewLifecycleOwner, Observer {

            weatherAdapter.setItem(it)

        })

        viewModel.liveDataTideList.observe(viewLifecycleOwner, Observer {

            tideAdapter.setItem(it)

        })

        viewModel.liveDataIndex.observe(viewLifecycleOwner, Observer {

            binding.conditionIndexAmTotalImage.setImageResource(it.amTotalImage)
            binding.conditionIndexPmTotalImage.setImageResource(it.pmTotalImage)

        })

        viewModel.liveDataSearchLocationList.observe(viewLifecycleOwner, Observer {

            searchLocationAdapter.setItem(it)

        })

        viewModel.liveDataSearchLocation.observe(viewLifecycleOwner, Observer {

            conditionEditor.putString("location", it.location)
            conditionEditor.putString("obsCode", it.obsCode)
            conditionEditor.putString("lat", it.lat)
            conditionEditor.putString("lon", it.lon)
            conditionEditor.commit()

        })

        viewModel.liveDataFishList.observe(viewLifecycleOwner, Observer {

            selectFishAdapter.setItem(it)

        })

        viewModel.liveDataLoadingStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.conditionTabCombineButton.isClickable = false
                binding.conditionTabWeatherButton.isClickable = false
                binding.conditionTabTideButton.isClickable = false
                binding.conditionTabIndexButton.isClickable = false
                binding.conditionSelectLocationButton.isClickable = false
                binding.conditionSelectDateButton.isClickable = false
                binding.conditionSelectFishButton.isClickable = false

                if (binding.conditionCombineLayout.visibility == View.VISIBLE) {

                    previousLayout = "combine"
                    binding.conditionCombineLayout.visibility = View.GONE


                } else if (binding.conditionWeatherLayout.visibility == View.VISIBLE) {

                    previousLayout = "weather"
                    binding.conditionWeatherLayout.visibility = View.GONE

                } else if (binding.conditionTideLayout.visibility == View.VISIBLE) {

                    previousLayout = "tide"
                    binding.conditionTideLayout.visibility = View.GONE

                }
                Log.d(TAG, "previous1 : $previousLayout")

                binding.conditionLoadingLayout.visibility = View.VISIBLE
                binding.conditionLoadingRightImage.visibility = View.VISIBLE
                binding.conditionLoadingRightImage.startAnimation(loadingAnimationRight)
                loadingAnimationStatus = true

                animationThread = thread {

                    try {

                        while (loadingAnimationStatus) {

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                Log.d(TAG, "이동 시작 1")
                                binding.conditionLoadingRightImage.visibility = View.GONE
                                binding.conditionLoadingLeftImage.visibility = View.VISIBLE
                                binding.conditionLoadingLeftImage.startAnimation(
                                    loadingAnimationLeft
                                )

                            }

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                Log.d(TAG, "이동 시작 2")
                                binding.conditionLoadingLeftImage.visibility = View.GONE
                                binding.conditionLoadingRightImage.visibility = View.VISIBLE
                                binding.conditionLoadingRightImage.startAnimation(
                                    loadingAnimationRight
                                )

                            }

                        }

                    } catch (e: InterruptedException) {

                        loadingAnimationStatus = false

                        Handler(Looper.getMainLooper()).post {

                            binding.conditionLoadingRightImage.clearAnimation()
                            binding.conditionLoadingLeftImage.clearAnimation()
                            binding.conditionLoadingRightImage.visibility = View.GONE
                            binding.conditionLoadingLeftImage.visibility = View.GONE
                            binding.conditionLoadingLayout.visibility = View.GONE

                        }

                    }

                }

            } else {

                binding.conditionTabCombineButton.isClickable = true
                binding.conditionTabWeatherButton.isClickable = true
                binding.conditionTabTideButton.isClickable = true
                binding.conditionTabIndexButton.isClickable = true
                binding.conditionSelectLocationButton.isClickable = true
                binding.conditionSelectDateButton.isClickable = true
                binding.conditionSelectFishButton.isClickable = true


                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }
                Log.d(TAG, "previous2 : $previousLayout")

                when (previousLayout) {

                    "combine" -> {

                        binding.conditionCombineLayout.visibility = View.VISIBLE

                    }

                    "weather" -> {

                        binding.conditionWeatherLayout.visibility = View.VISIBLE

                    }

                    "tide" -> {

                        binding.conditionTideLayout.visibility = View.VISIBLE

                    }

                }

            }

        })

        viewModel.liveDataBasicWeatherList.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).setWeatherList(it)

        })

    } // observeLiveData()


    fun checkedLocationSharedPreference() {

        conditionSharedPreferences =
            requireActivity().getSharedPreferences("location", AppCompatActivity.MODE_PRIVATE)
        conditionEditor = conditionSharedPreferences.edit()

        var locationValue: String = conditionSharedPreferences.getString("location", "").toString()
        var obsCodeValue: String = conditionSharedPreferences.getString("obsCode", "").toString()
        var lat: String = conditionSharedPreferences.getString("lat", "").toString()
        var lon: String = conditionSharedPreferences.getString("lon", "").toString()

        if (locationValue == "") {
            locationValue = "영흥도"
            obsCodeValue = "DT_0043"
            lat = "37"
            lon = "126"
        }

        searchLocation = SearchLocation(locationValue, obsCodeValue, lat, lon)

    } // checkedLocationSharedPreference()


    fun changeLayout(layoutName: String) {

        when (layoutName) {

            "combine" -> {
                binding.conditionCombineTitleLayout.visibility = View.VISIBLE
                binding.conditionWeatherTitleLayout.visibility = View.GONE
                binding.conditionTideTitleLayout.visibility = View.GONE
                binding.conditionIndexTitleLayout.visibility = View.GONE
                binding.conditionSelectDateTitleLayout.visibility = View.GONE
                binding.conditionSearchLocationTitleLayout.visibility = View.GONE
                binding.conditionSelectFishTitleLayout.visibility = View.GONE

                binding.conditionSelectLayout.visibility = View.VISIBLE
                binding.conditionTabLayout.visibility = View.VISIBLE
                binding.conditionSelectFishButton.visibility = View.GONE
                binding.conditionSelectFishView.visibility = View.GONE

                binding.conditionCombineLayout.visibility = View.VISIBLE
                binding.conditionWeatherLayout.visibility = View.GONE
                binding.conditionTideLayout.visibility = View.GONE
                binding.conditionIndexLayout.visibility = View.GONE
                binding.conditionSelectDateLayout.visibility = View.GONE
                binding.conditionSearchLocationLayout.visibility = View.GONE
                binding.conditionSelectFishLayout.visibility = View.GONE

                binding.conditionTabCombineButton.visibility = View.GONE
                binding.conditionTabWeatherButton.visibility = View.VISIBLE
                binding.conditionTabTideButton.visibility = View.VISIBLE
                binding.conditionTabIndexButton.visibility = View.VISIBLE
                binding.conditionTabCombineBlock.visibility = View.VISIBLE
                binding.conditionTabWeatherBlock.visibility = View.GONE
                binding.conditionTabTideBlock.visibility = View.GONE
                binding.conditionTabIndexBlock.visibility = View.GONE
            }

            "weather" -> {
                binding.conditionCombineTitleLayout.visibility = View.GONE
                binding.conditionWeatherTitleLayout.visibility = View.VISIBLE
                binding.conditionTideTitleLayout.visibility = View.GONE
                binding.conditionIndexTitleLayout.visibility = View.GONE
                binding.conditionSelectDateTitleLayout.visibility = View.GONE
                binding.conditionSearchLocationTitleLayout.visibility = View.GONE
                binding.conditionSelectFishTitleLayout.visibility = View.GONE

                binding.conditionSelectLayout.visibility = View.VISIBLE
                binding.conditionTabLayout.visibility = View.VISIBLE
                binding.conditionSelectFishButton.visibility = View.GONE
                binding.conditionSelectFishView.visibility = View.GONE

                binding.conditionCombineLayout.visibility = View.GONE
                binding.conditionWeatherLayout.visibility = View.VISIBLE
                binding.conditionTideLayout.visibility = View.GONE
                binding.conditionIndexLayout.visibility = View.GONE
                binding.conditionSelectDateLayout.visibility = View.GONE
                binding.conditionSearchLocationLayout.visibility = View.GONE
                binding.conditionSelectFishLayout.visibility = View.GONE

                binding.conditionTabCombineButton.visibility = View.VISIBLE
                binding.conditionTabWeatherButton.visibility = View.GONE
                binding.conditionTabTideButton.visibility = View.VISIBLE
                binding.conditionTabIndexButton.visibility = View.VISIBLE
                binding.conditionTabCombineBlock.visibility = View.GONE
                binding.conditionTabWeatherBlock.visibility = View.VISIBLE
                binding.conditionTabTideBlock.visibility = View.GONE
                binding.conditionTabIndexBlock.visibility = View.GONE
            }

            "tide" -> {
                binding.conditionCombineTitleLayout.visibility = View.GONE
                binding.conditionWeatherTitleLayout.visibility = View.GONE
                binding.conditionTideTitleLayout.visibility = View.VISIBLE
                binding.conditionIndexTitleLayout.visibility = View.GONE
                binding.conditionSelectDateTitleLayout.visibility = View.GONE
                binding.conditionSearchLocationTitleLayout.visibility = View.GONE
                binding.conditionSelectFishTitleLayout.visibility = View.GONE

                binding.conditionSelectLayout.visibility = View.VISIBLE
                binding.conditionTabLayout.visibility = View.VISIBLE
                binding.conditionSelectFishButton.visibility = View.GONE
                binding.conditionSelectFishView.visibility = View.GONE

                binding.conditionCombineLayout.visibility = View.GONE
                binding.conditionWeatherLayout.visibility = View.GONE
                binding.conditionTideLayout.visibility = View.VISIBLE
                binding.conditionIndexLayout.visibility = View.GONE
                binding.conditionSelectDateLayout.visibility = View.GONE
                binding.conditionSearchLocationLayout.visibility = View.GONE
                binding.conditionSelectFishLayout.visibility = View.GONE

                binding.conditionTabCombineButton.visibility = View.VISIBLE
                binding.conditionTabWeatherButton.visibility = View.VISIBLE
                binding.conditionTabTideButton.visibility = View.GONE
                binding.conditionTabIndexButton.visibility = View.VISIBLE
                binding.conditionTabCombineBlock.visibility = View.GONE
                binding.conditionTabWeatherBlock.visibility = View.GONE
                binding.conditionTabTideBlock.visibility = View.VISIBLE
                binding.conditionTabIndexBlock.visibility = View.GONE
            }

            "index" -> {
                binding.conditionCombineTitleLayout.visibility = View.GONE
                binding.conditionWeatherTitleLayout.visibility = View.GONE
                binding.conditionTideTitleLayout.visibility = View.GONE
                binding.conditionIndexTitleLayout.visibility = View.VISIBLE
                binding.conditionSelectDateTitleLayout.visibility = View.GONE
                binding.conditionSearchLocationTitleLayout.visibility = View.GONE
                binding.conditionSelectFishTitleLayout.visibility = View.GONE

                binding.conditionSelectLayout.visibility = View.VISIBLE
                binding.conditionTabLayout.visibility = View.VISIBLE
                binding.conditionSelectFishButton.visibility = View.VISIBLE
                binding.conditionSelectFishView.visibility = View.VISIBLE

                binding.conditionCombineLayout.visibility = View.GONE
                binding.conditionWeatherLayout.visibility = View.GONE
                binding.conditionTideLayout.visibility = View.GONE
                binding.conditionIndexLayout.visibility = View.VISIBLE
                binding.conditionSelectDateLayout.visibility = View.GONE
                binding.conditionSearchLocationLayout.visibility = View.GONE
                binding.conditionSelectFishLayout.visibility = View.GONE

                binding.conditionTabCombineButton.visibility = View.VISIBLE
                binding.conditionTabWeatherButton.visibility = View.VISIBLE
                binding.conditionTabTideButton.visibility = View.VISIBLE
                binding.conditionTabIndexButton.visibility = View.GONE
                binding.conditionTabCombineBlock.visibility = View.GONE
                binding.conditionTabWeatherBlock.visibility = View.GONE
                binding.conditionTabTideBlock.visibility = View.GONE
                binding.conditionTabIndexBlock.visibility = View.VISIBLE
            }

            "selectDate" -> {
                binding.conditionCombineTitleLayout.visibility = View.GONE
                binding.conditionWeatherTitleLayout.visibility = View.GONE
                binding.conditionTideTitleLayout.visibility = View.GONE
                binding.conditionIndexTitleLayout.visibility = View.GONE
                binding.conditionSelectDateTitleLayout.visibility = View.VISIBLE
                binding.conditionSearchLocationTitleLayout.visibility = View.GONE
                binding.conditionSelectFishTitleLayout.visibility = View.GONE

                binding.conditionSelectLayout.visibility = View.GONE
                binding.conditionTabLayout.visibility = View.GONE

                binding.conditionCombineLayout.visibility = View.GONE
                binding.conditionWeatherLayout.visibility = View.GONE
                binding.conditionTideLayout.visibility = View.GONE
                binding.conditionIndexLayout.visibility = View.GONE
                binding.conditionSelectDateLayout.visibility = View.VISIBLE
            }

            "searchLocation" -> {
                binding.conditionCombineTitleLayout.visibility = View.GONE
                binding.conditionWeatherTitleLayout.visibility = View.GONE
                binding.conditionTideTitleLayout.visibility = View.GONE
                binding.conditionIndexTitleLayout.visibility = View.GONE
                binding.conditionSelectDateTitleLayout.visibility = View.GONE
                binding.conditionSearchLocationTitleLayout.visibility = View.VISIBLE
                binding.conditionSelectFishTitleLayout.visibility = View.GONE

                binding.conditionSelectLayout.visibility = View.GONE
                binding.conditionTabLayout.visibility = View.GONE

                binding.conditionCombineLayout.visibility = View.GONE
                binding.conditionWeatherLayout.visibility = View.GONE
                binding.conditionTideLayout.visibility = View.GONE
                binding.conditionIndexLayout.visibility = View.GONE
                binding.conditionSearchLocationLayout.visibility = View.VISIBLE
            }

            "selectFish" -> {
                binding.conditionCombineTitleLayout.visibility = View.GONE
                binding.conditionWeatherTitleLayout.visibility = View.GONE
                binding.conditionTideTitleLayout.visibility = View.GONE
                binding.conditionIndexTitleLayout.visibility = View.GONE
                binding.conditionSelectDateTitleLayout.visibility = View.GONE
                binding.conditionSearchLocationTitleLayout.visibility = View.GONE
                binding.conditionSelectFishTitleLayout.visibility = View.VISIBLE

                binding.conditionSelectLayout.visibility = View.GONE
                binding.conditionTabLayout.visibility = View.GONE

                binding.conditionCombineLayout.visibility = View.GONE
                binding.conditionWeatherLayout.visibility = View.GONE
                binding.conditionTideLayout.visibility = View.GONE
                binding.conditionIndexLayout.visibility = View.GONE
                binding.conditionSelectFishLayout.visibility = View.VISIBLE
            }

        }

    } // changeLayout()


    class TodayDecorator(context: Context) : DayViewDecorator {

        val drawable: Drawable =
            ContextCompat.getDrawable(context, R.drawable.calendar_selector_color)!!

        override fun shouldDecorate(day: CalendarDay?): Boolean {

            return true

        } // shouldDevorate()


        override fun decorate(view: DayViewFacade?) {

            view!!.setSelectionDrawable(drawable)

        } // decorate()

    } // DayDecorator


    class SundayDecorator : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            val sunday = day?.date?.with(org.threeten.bp.DayOfWeek.SUNDAY)?.dayOfMonth
            return sunday == day?.day
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.RED) {})
        }

    }


    class SaturdayDecorator : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            val saturday = day?.date?.with(org.threeten.bp.DayOfWeek.SATURDAY)?.dayOfMonth
            return saturday == day?.day
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.BLUE) {})
        }

    }


    class MinMaxDecorator(max: CalendarDay, min: CalendarDay) : DayViewDecorator {

        val maxDay = max
        val minDay = min

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return return  (day!!.month != minDay.month) || (day.day > maxDay.day) || (day.day < minDay.day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#D9D9D9")) {})
            view?.setDaysDisabled(true)
        }

    }


}