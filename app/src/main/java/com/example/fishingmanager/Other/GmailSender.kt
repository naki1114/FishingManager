package com.example.fishingmanagerclone.Other

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GmailSender : Authenticator() {

    val googleId : String = ""
    val googlePw : String = ""

    override fun getPasswordAuthentication() : PasswordAuthentication {

        return PasswordAuthentication(googleId, googlePw)

    } // getPasswordAuthentication()


    fun sendMail(title : String, content : String, receiver : String) {

        val properties : Properties = Properties()
        properties.setProperty("mail.transport.protocol", "smtp")
        properties.setProperty("mail.host", "smtp.gmail.com")
        properties.put("mail.smtp.auth", "true")
        properties.put("mail.smtp.port", "465")
        properties.put("mail.smtp.socketFactory.port", "465")
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        properties.put("mail.smtp.socketFactory.fallback", "false")
        properties.setProperty("mail.smtp.quitwait", "false")

        val session = Session.getDefaultInstance(properties, this@GmailSender)

        val message = MimeMessage(session)
        message.sender = InternetAddress(googleId)
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver))

        message.subject = title
        message.setText(content)

        Transport.send(message)

    } // sendMail()


}


