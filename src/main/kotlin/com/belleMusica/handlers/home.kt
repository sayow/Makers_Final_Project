package com.belleMusica.handlers

import com.belleMusica.viewmodel.FeedViewModel
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import templateRenderer

fun getHomePage(request: Request): Response{
    val feeds = FeedViewModel("Ahhhh")
    val theContent = templateRenderer(feeds)
    return  Response(Status.OK).body(theContent)
}