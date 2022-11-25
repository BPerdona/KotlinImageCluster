
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.color.Color
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import smile.clustering.kmeans
import java.io.File

fun main(){
    // Root path
    val rootPath = System.getProperty("user.dir")+"\\src\\main\\kotlin"

    // Select Image
    val imageFile = File("$rootPath\\images\\redhot.png")

    // Load Image
    val image = ImmutableImage.loader().fromFile(imageFile)

    // Create the data
    val data = arrayOfNulls<DoubleArray>(image.colors().size)
    image.colors().forEachIndexed() { index, it ->
        data[index] = doubleArrayOf(it.red.toDouble(), it.green.toDouble(), it.blue.toDouble())
    }

    // Run kmeans
    val result = kmeans(data.requireNoNulls(), 8)

    // Print centroids result
    result.centroids.forEachIndexed { index, centroid ->
        val (r, g, b) = centroid
        println("Centroid $index -> R: $r | G: $g | B: $b")
    }

    // Copying image
    val imagemFinal = image.copy()

    // Convert Image
    result.y.forEachIndexed { index, value ->
        imagemFinal.setColor(index){
            Color {
                RGBColor(
                    result.centroids[value][0].toInt(),
                    result.centroids[value][1].toInt(),
                    result.centroids[value][2].toInt()
                )
            }.toRGB()
        }
    }

    // Save Image
    imagemFinal.output(PngWriter(), "$rootPath\\images\\alt.png")
}