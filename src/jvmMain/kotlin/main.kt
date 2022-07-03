import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamResolution
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.Korge
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.image
import com.soywiz.korim.awt.toAwtNativeImage
import com.soywiz.korim.awt.toBMP32
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.bitmap.copy
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.color.BGRA
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.ColorsExt
import com.soywiz.korim.color.RGBA
import com.soywiz.korma.geom.degrees
import java.awt.Dimension
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
suspend fun main() = Korge(width = 1920, height = 1080, bgcolor = Colors["#2b2b2b"]) {
    val minDegrees = (-16).degrees
    val maxDegrees = (+16).degrees

    //    val image = image(resourcesVfs["korge.png"].readBitmap()) {
    //        rotation = maxDegrees
    //        anchor(.5, .5)
    //        scale(.8)
    //        position(256, 256)
    //    }

    //    val webcams = Webcam.getWebcams()
    //    println("webcams: $webcams")
    //    println(webcams[1].name)

    val nonStandardResolutions = arrayOf(
        WebcamResolution.FHD.size,
    )

    val webcam = Webcam.getWebcamByName("USB Video 1")
    println("webcam: $webcam")
    webcam.setCustomViewSizes(*nonStandardResolutions)
    webcam.viewSize = WebcamResolution.FHD.size
    webcam.open(true)

    val bitmap = Bitmap32(1920, 1080)
    val image = image(bitmap)

    addUpdater {
        val webcamImage = measureTimedValue {
            webcam.image
        }

        val toAwtNativeImageTime = measureTimedValue {
            webcamImage.value.toAwtNativeImage()
        }

        val updateBitmapTime = measureTime {
            toAwtNativeImageTime.value.copyUnchecked(0, 0, bitmap, 0, 0, 1920, 1080)
//            webcamImage.value.toAwtNativeImage().copyUnchecked()
            bitmap.contentVersion++
//            image.bitmap = webcamImage.value.toAwtNativeImage().slice()
        }
        println("webcamImage: ${webcamImage.duration}")
        println("toAwtNativeImageTime: ${toAwtNativeImageTime.duration}")
        println("updateBitmapTime: $updateBitmapTime")
    }

    //    addFixedUpdater(TimeSpan(100.0)) {
    //        val updateTime1 = measureTime {
    //            image.removeFromParent()
    //            //                        image = image(webcam.image.toAwtNativeImage().slice())
    //            //            image.bitmap = webcam.image.toAwtNativeImage().slice()
    //        }
    //
    //        val updateTime2 = measureTime {
    //            //            image.removeFromParent()
    //                        image = image(webcam.image.toAwtNativeImage().slice())
    ////            image.bitmap = webcam.image.toAwtNativeImage().slice()
    //        }
    //        println("updateTime1: $updateTime1")
    //        println("updateTime2: $updateTime2")
    //    }

    //    ImageIO.write(webcam.image, "PNG", File("hello-world.png"))

    //    while (true) {
    //        image.tween(image::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
    //        image.tween(image::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
    //    }
}