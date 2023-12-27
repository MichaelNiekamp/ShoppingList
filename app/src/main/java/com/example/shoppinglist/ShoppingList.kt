package com.example.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly


data class ShoppingItem(val id: Int, var name: String, var amount: Int, var isEditing: Boolean = false)

@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }


    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = { showDialog = true } , modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add Item ")
            Icon(Icons.Filled.Add, contentDescription = null)
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(sItems.size){
                item ->
                if (sItems[item].isEditing) {
                    ShoppingItemEditor(item = sItems[item], onEditComplete = {
                        editedName, editedAmount ->
                        sItems = sItems.map {
                            it.copy(isEditing = false)
                        }
                        val editedItem = sItems.find { it.id == sItems[item].id }
                        editedItem?.let {
                            it.name = editedName
                            it.amount = editedAmount
                        }
                    })
                } else {
                    ShoppingListItem(item = sItems[item], onEditClick = {
                        sItems = sItems.map{it.copy(isEditing = (it.id == sItems[item].id))}
                    }, onDeleteClick = {
                        sItems = sItems - sItems[item]
                    })
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, 
            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Button(onClick = {
                                    if (itemName.isNotEmpty() && itemQuantity.isNotEmpty() && itemQuantity.isDigitsOnly()) {
                                        sItems = sItems + ShoppingItem(sItems.size, itemName, itemQuantity.toInt())
                                        showDialog = false
                                        itemName = ""
                                        itemQuantity = ""
                                    }
                                }) {
                                    Text(text = "Add")
                                }
                                Button(onClick = { showDialog = false }) {
                                    Text(text = "Cancel")
                                }
                            }
                            },
            title = { Text(text = "Add Item") }, 
            text = {
                Column {
                    OutlinedTextField(value = itemName, onValueChange = { itemName = it }, singleLine = true, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp))
                    OutlinedTextField(value = itemQuantity, onValueChange = { itemQuantity = it }, singleLine = true, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp))
            } }
        )
    }
}



@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedAmount by remember { mutableStateOf(item.amount.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.LightGray)
        .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column {
            OutlinedTextField(value = editedName, onValueChange = { editedName = it }, singleLine = true, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
            OutlinedTextField(value = editedAmount, onValueChange = { editedAmount = it }, singleLine = true, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
            Row {
                Button(onClick = { isEditing = false; onEditComplete(item.name, item.amount) } ) {
                    Text(text = "Cancel")
            }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { isEditing = false; onEditComplete(editedName, editedAmount.toIntOrNull() ?: 1) }) {
                    Text(text = "Save")

            }
        }

    }
}
}


@Composable
fun ShoppingListItem(item: ShoppingItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit, ) {
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(border = BorderStroke(2.dp, Color.Gray), shape = RoundedCornerShape(20)), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.amount}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = { onEditClick() }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = { onDeleteClick() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}