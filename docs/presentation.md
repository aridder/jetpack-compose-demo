---
marp: true
---

# Jetpack Compose 

---

Jetpack Compose er bygget rundt bruk av "Composable functions". Gir oss mulighet til å beskrive UI programatisk

```kotlin
@Composable
fun EnturTextField(text: String) {
    TextField(
        value = text, 
        modifier = Modifier.background(EnturBackgroundColor))
}
```

---

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnturTextField("Hello world!")
        }
    }
}
```


---
# Layout

![bg right:33%](./images/04-overlap.png)

```
@Composable
fun NewsStory() {
    Text("A day in Shark Fin Cove")
    Text("Davenport, California")
    Text("December 2018")
}
```
---

# Layouts

![bg right:33%](./images/05-column.png)

```kotlin
@Composable
fun NewsStory() {
    Column {
        Text("A day in Shark Fin Cove")
        Text("Davenport, California")
        Text("December 2018")
    }
}
```
---


![bg right:33%](./images/07-column-styled.png)

Man kan konfigurere layouten ved å bruke f.eks. "modifier".

```kotlin
@Composable
fun NewsStory() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("A day in Shark Fin Cove")
        Text("Davenport, California")
        Text("December 2018")
    }
}
```
---
# State

```kotlin
@Composable
fun HelloContent() {
   Column(modifier = Modifier.padding(16.dp)) {
       Text(
           text = "Hello!",
           modifier = Modifier.padding(bottom = 8.dp),
           style = MaterialTheme.typography.h5
       )
       OutlinedTextField(
           value = "",
           onValueChange = { },
           label = { Text("Name") }
       )
   }
}
```

---

# Composition and recomposition

_Composition_ beskriver UI og er beskrevet av @Composables i appen. Trestruktur. Når state endres, trigges _recomposition_

_Recomposition_ er prosessens som kjører @Composables der state har endret seg for å oppdatere _composition_

---
- _remember_ lar composables huske verdier fra en _composition_ og returnerer denne i _recomposition_
- _mutableStateOf_ er en observable type i Compose. Alle endringer i denne vil trigge en _recomposition_

```kotlin
@Composable
fun HelloContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        var name by remember { mutableStateOf("") }
        Text(
            text = "Hello",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
    }
}
```

---
```kotlin
@Composable
fun HelloScreen() {
    var name by rememberSaveable { mutableStateOf("") }

    HelloContent(name = name, onNameChange = { name = it })
}

@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello, $name",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") }
        )
    }
}
````