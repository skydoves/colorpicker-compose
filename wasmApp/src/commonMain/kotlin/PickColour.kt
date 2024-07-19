import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.*

@Composable
fun PickColour() {
  val controller = rememberColorPickerController()
  var currentColorEnvelope by remember {
    mutableStateOf(
      ColorEnvelope(
        Color.Green,
        "ff00ff00",
        false
      )
    )
  }

  Column(
    modifier = Modifier.fillMaxSize().background(Color.Black),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    HsvColorPicker(
      initialColor = currentColorEnvelope.color,
      modifier = Modifier.padding(10.dp).size(300.dp),
      controller = controller,
      onColorChanged = { colorEnvelope: ColorEnvelope ->
        currentColorEnvelope = colorEnvelope
      }
    )

    Spacer(Modifier.height(8.dp))

    AlphaSlider(
      modifier = Modifier
        .height(35.dp)
        .width(400.dp),
      controller = controller,
    )

    Spacer(Modifier.height(8.dp))

    BrightnessSlider(
      modifier = Modifier
        .height(35.dp)
        .width(400.dp),
      controller = controller,
    )

    Spacer(Modifier.height(8.dp))

    SelectionContainer {
      Text(
        currentColorEnvelope.hexCode,
        color = currentColorEnvelope.color
      )
    }

    AlphaTile(
      modifier = Modifier
        .size(80.dp)
        .clip(RoundedCornerShape(6.dp)),
      controller = controller
    )
  }
}
