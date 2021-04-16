package com.example.composedemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Menu : Screen("menu", R.string.menu)
    object BasicLayout : Screen("basic_layout", R.string.basic_layout)
    object Arrangment : Screen("arrangment", R.string.arrangment)
    object Padding : Screen("padding", R.string.padding)
    object OnClick : Screen("onclick", R.string.onclick)
    object List : Screen("list", R.string.list)
    object ConstraintLayout : Screen("constraint_layout", R.string.constraint_layout)
    object Remember : Screen("remember", R.string.remember)
    object RememberMutableStateOf : Screen("remember_ms", R.string.remember_mutable_state_of)
    object SharedState : Screen("shared_state", R.string.shared_state)
    object DerivedStateOf : Screen("derived_state_of", R.string.derived_state_of)
    object CollectAsState : Screen("as_state", R.string.as_state)
    object LaunchedEffect : Screen("launched_effect", R.string.launched_effect)
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberNavController()
            var isInDarkMode by remember { mutableStateOf(false) }
            val navigateToMenuAction = { navController.navigate(Screen.Menu.route) }

            ComposeDemoTheme(darkTheme = isInDarkMode) {

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                ) {
                    DarkModeHeader(
                        isInDarkMode = isInDarkMode,
                        navigateToMenu = navigateToMenuAction,
                        onToggleSwitch = { isInDarkMode = !isInDarkMode })

                    Content(navController = navController)
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun Content(navController: NavHostController) {
    Row(modifier = Modifier.fillMaxSize()) {

        var screensAndVisited by remember {
            mutableStateOf(
                mapOf(
                    Screen.BasicLayout to false,
                    Screen.Padding to false,
                    Screen.Arrangment to false,
                    Screen.OnClick to false,
                    Screen.List to false,
                    Screen.ConstraintLayout to false,
                    Screen.Remember to false,
                    Screen.RememberMutableStateOf to false,
                    Screen.DerivedStateOf to false,
                    Screen.SharedState to false,
                    Screen.CollectAsState to false,
                    Screen.LaunchedEffect to false
                )
            )
        }

        val visitScreen: (Screen) -> Unit = {
            val map = screensAndVisited.toMutableMap()
            map[it] = true
            screensAndVisited = map
        }

        NavHost(navController, startDestination = Screen.Menu.route) {
            composable(Screen.Menu.route) {
                MenuScreen(navController,
                    screensAndVisited,
                    visitScreen
                )
            }
            composable(Screen.BasicLayout.route) { BasicLayout() }
            composable(Screen.Arrangment.route) { Arrangment() }
            composable(Screen.Padding.route) { Padding() }
            composable(Screen.OnClick.route) { OnClick() }
            composable(Screen.List.route) { List() }
            composable(Screen.ConstraintLayout.route) { ConstraintLayoutScreen() }
            composable(Screen.Remember.route) { Remember(number = 1000) }
            composable(Screen.RememberMutableStateOf.route) { RememberMutableStateOf() }
            composable(Screen.SharedState.route) { SharedState() }
            composable(Screen.DerivedStateOf.route) { DerivedStateOf() }
            composable(Screen.CollectAsState.route) { CollectAsState() }
            composable(Screen.LaunchedEffect.route) { LaunchedEffectScreen() }
        }
    }
}



















































@Composable
fun MenuScreen(
    navController: NavHostController,
    screensAndVisited: Map<Screen, Boolean>,
    visitScreen: (Screen) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Meny", modifier = Modifier.padding(4.dp), fontSize = 20.sp)

        screensAndVisited.forEach { (screen, visited) ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    visitScreen(screen)
                    navController.navigate(screen.route)
                },
                    modifier = Modifier.padding(4.dp)) {
                    Text(text = stringResource(id = screen.resourceId))
                }
                if (visited) {
                    Icon(Icons.Default.Check, tint = Color.Green, contentDescription = null)
                }
            }
        }
    }
}




















































@Composable
private fun DarkModeHeader(
    isInDarkMode: Boolean,
    navigateToMenu: () -> Unit,
    onToggleSwitch: (Boolean) -> Unit,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.background)
        .height(40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Darkmode:", color = MaterialTheme.colors.onBackground)

        Switch(
            checked = isInDarkMode,
            onCheckedChange = onToggleSwitch)

        Button(onClick = navigateToMenu) {
            Text(text = "Menu")
        }
    }
}





















































@Composable
fun BasicLayout() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Greeting(name = "Demotime")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}





















































@Composable
fun Padding() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
    ) {
        Text(text = "Padding 0dp", modifier = Modifier.background(Color.Red))

        Text(text = "Padding 48 dp",
            modifier = Modifier
                .padding(48.dp)
                .background(Color.Blue))

        Text(text = "Start padding 12dp", modifier = Modifier.padding(start = 12.dp))

        Text(text = "Padding 24dp", Modifier.padding(24.dp))
    }
}





























































@Composable
fun Arrangment() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(id = Screen.BasicLayout.resourceId))
        }

        Column {
            Text(text = "No Arrangment")
            Row(modifier = Modifier.background(Color.Cyan)) {
                Tekst123()
            }
        }

        Column {
            Text(text = "No Arrangment and fill max width")
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Cyan)
            ) {
                Tekst123()
            }
        }

        Column {
            Text(text = "Arrangement center")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow),
                horizontalArrangement = Arrangement.Center,
            ) {
                Tekst123()
            }
        }

        Column {
            Text(text = "Arrangement SpaceBetween")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Cyan),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Tekst123()
            }
        }

        Column {
            Text(text = "Arrangement SpaceAround")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Tekst123()
            }
        }

        Column {
            Text(text = "Arrangement SpaceEvenly")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Cyan),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Tekst123()
            }
        }
    }
}

@Composable
fun Tekst123() {
    Text(text = "Tekst1", Modifier.background(Color.Blue))
    Text(text = "Tekst2", Modifier.background(Color.Red))
    Text(text = "Tekst3", Modifier.background(Color.Green))
}




















































@Composable
fun OnClick() {
    val context = LocalContext.current

    val shapes = listOf(
        RoundedCornerShape(percent = 50),
        RoundedCornerShape(percent = 30),
        RoundedCornerShape(percent = 10),
        CutCornerShape(
            topStart = 100.dp,
            bottomEnd = 100.dp
        ),
        CutCornerShape(
            topEnd = 30.dp,
            bottomEnd = 10.dp
        )
    )

    var currentShape by remember { mutableStateOf(shapes.first()) }

    Column(Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .size(300.dp)
            .background(MaterialTheme.colors.primary, shape = currentShape)
            .clickable {
                currentShape = shapes.random()
                Toast
                    .makeText(context, "You clicked me :)", Toast.LENGTH_SHORT)
                    .show()
            }
        ) {
            Text(text = "Click me",
                color = MaterialTheme.colors.primaryVariant)
        }
    }
}

















































@Composable
fun FoodItem(food: Food) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(MaterialTheme.colors.secondary))
    {
        Icon(Icons.Default.Fastfood,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(4.dp))
        Column(modifier = Modifier
            .padding(start = 16.dp)) {
            Text(text = "Weight: ${food.weight}")
            Text(text = "Category: ${food.category}")
        }

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(text = "Name: ${food.name}")
            Text(text = "Price: ${food.price}")
        }
    }
}

@Composable
fun CarItem(car: Car) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colors.primary),
    ) {
        Icon(Icons.Default.CarRental,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp))
        Text(text = "Brand: ${car.name}")
        Text(text = "Price: ${car.price}", modifier = Modifier.padding(start = 16.dp))
    }
}

interface Item

data class Car(
    val name: String,
    val price: Int,
) : Item

data class Food(
    val weight: Float,
    val name: String,
    val price: Int,
    val category: String,
) : Item

@ExperimentalFoundationApi
@Composable
fun List() {
    val state = listOf(
        Food(1.2f, "Cod", 109, "Fish"),
        Food(1.2f, "Cod", 109, "Fish"),
        Food(1.2f, "Cod", 109, "Fish"),
        Food(2.1f, "Cheddar", 89, "Cheese"),
        Car("Audi", 350_000),
        Car("BMW", 370_000),
        Car("VW", 270_000),
        Food(3.2f, "Carrot", 10, "Vegetable"),
        Food(1.2f, "Cod", 109, "Fish"),
        Food(2.1f, "Cheddar", 89, "Cheese"),
        Car("Audi", 350_000),
        Car("BMW", 370_000),
        Car("VW", 270_000),
        Food(2.1f, "Cheddar", 89, "Cheese"),
        Car("Audi", 350_000),
        Car("VW", 270_000),
        Food(3.2f, "Carrot", 10, "Vegetable"),
        Car("BMW", 370_000),
        Food(2.1f, "Cheddar", 89, "Cheese"),
        Car("Audi", 350_000),
        Car("VW", 270_000),
        Food(2.1f, "Cheddar", 89, "Cheese"),
        Car("Audi", 350_000),
        Car("BMW", 370_000),
        Food(3.2f, "Carrot", 10, "Vegetable"),
        Food(1.2f, "Cod", 109, "Fish"),
        Car("VW", 270_000),
        Food(2.1f, "Cheddar", 89, "Cheese"),
        Car("Audi", 350_000),
        Car("BMW", 370_000),
        Food(3.2f, "Carrot", 10, "Vegetable"),
        Food(1.2f, "Cod", 109, "Fish"),
        Car("VW", 270_000),
        Food(3.2f, "Carrot", 10, "Vegetable"),
        Food(2.1f, "Cheddar", 89, "Cheese"),
        Car("Audi", 350_000),
        Car("BMW", 370_000),
        Car("VW", 270_000),
        Food(3.2f, "Carrot", 10, "Vegetable"),
        Food(1.2f, "Cod", 109, "Fish"),
        Food(2.1f, "Cheddar", 89, "Cheese"),
        Car("BMW", 370_000),
        Food(3.2f, "Carrot", 10, "Vegetable"),
        Car("VW", 270_000),
        Food(2.1f, "Cheddar", 89, "Cheese"),
        Car("Audi", 350_000),
        Car("BMW", 370_000),
        Car("VW", 270_000),
        Food(3.2f, "Carrot", 10, "Vegetable"),
    )

    val scrollState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
            stickyHeader {
                Text(modifier = Modifier.background(MaterialTheme.colors.background),
                    color = MaterialTheme.colors.onBackground,
                    text = "ScrollState: ${scrollState.firstVisibleItemIndex}")
            }
            items(state) { item ->
                when (item) {
                    is Food -> FoodItem(food = item)
                    is Car -> CarItem(car = item)
                }
                Divider(modifier = Modifier.fillMaxWidth().background(Color.Black).width(2.dp))
            }
        }
    }
}





















































@ExperimentalComposeUiApi
@Composable
fun ConstraintLayoutScreen() {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (nameText, text, bottomText) = createRefs()

        Text(text = "Johnsen", modifier = Modifier.constrainAs(nameText) {
            top.linkTo(parent.top, margin = 32.dp)
            end.linkTo(parent.end, margin = 16.dp)
        })

        Text(text = "Tekst", modifier = Modifier.constrainAs(text) {
            end.linkTo(nameText.start)
            top.linkTo(nameText.bottom)
        })

        Text(text = "123Heisann", modifier = Modifier.constrainAs(bottomText) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        })
    }
}





















































@Composable
fun Remember(number: Int) {
    val state = remember {
        heavyComputation(number)
    }

    Text(text = "Result of heavy calc: $state")
}

fun heavyComputation(number: Int): Int {
    return number * 2
}





















































@Composable
fun RememberMutableStateOf() {
    var state by remember {
        mutableStateOf(0)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "State er  $state")
        Button(onClick = { state += 1 }) {
            Text(text = "Increase")
        }

        Button(onClick = { state -= 1 }) {
            Text(text = "Decrease")
        }
    }
}


























































@Composable
fun DerivedStateOf() {
    var number by remember {
        mutableStateOf(0)
    }

    var name by remember {
        mutableStateOf("Fredrik")
    }

    val derived by derivedStateOf {
        "Det finnes $number $name i hele verden"
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = derived)

        Button(onClick = { number += 1 }) {
            Text(text = "Increase number")
        }

        TextField(value = name, onValueChange = { name = it })
    }
}

























































@Composable
fun SharedState() {
    var number by remember {
        mutableStateOf(0)
    }

    val degrees by derivedStateOf {
        (number * 10f) % 360
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .rotate(degrees)) {
        Component1(number = number, onNumberChange = { number = it })
        Component2(number = number)
    }

}

@Composable
fun Component1(number: Int, onNumberChange: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { onNumberChange(number + 1) }) {
            Text(text = "Increase number")
        }
    }
}

@Composable
fun Component2(number: Int) {
    val backgroundColor = if (number % 2 == 0) Color.Cyan else Color.Green
    val contentColor = contentColorFor(backgroundColor = backgroundColor)
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(backgroundColor)) {
        Text(text = number.toString(), color = contentColor)
    }
}






















































object DummyState {
    val amountLiveData = MutableLiveData(0)
    val nameFlow = MutableStateFlow("Fredrik")
}

@Composable
fun CollectAsState() {
    val amount: Int? by DummyState.amountLiveData.observeAsState()
    val name: String by DummyState.nameFlow.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Navn er $name")
        Text(text = "Amount er $amount")

        Button(onClick = {
            val currentAmount = amount ?: 1
            DummyState.amountLiveData.value = currentAmount + 1
        }) {
            Text(text = "Increase number")
        }

        TextField(value = name, onValueChange = { DummyState.nameFlow.value = it })
    }
}





















































@Composable
fun LaunchedEffectScreen() {
    val context = LocalContext.current
    var fetchError by remember { mutableStateOf(false) }
    var otherError by remember { mutableStateOf(false) }

    val someError by derivedStateOf { fetchError || otherError }

    LaunchedEffect(someError) {
        Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()
        delay(1000)
        fetchError = false
        otherError = false
    }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "fetch error: $fetchError")
        Text(text = "other error: $otherError")

        Button(onClick = { fetchError = true }) {
            Text(text = "Set fetch error = true")
        }

        Button(onClick = { otherError = true }) {
            Text(text = "Set error error = true")
        }

        Text(text = "some error: $someError",
            Modifier.background(if (someError) Color.Red else Color.Green))
    }
}












