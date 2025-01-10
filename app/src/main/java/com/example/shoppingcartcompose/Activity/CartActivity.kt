package com.example.shoppingcartcompose.Activity

import android.os.Bundle
import android.provider.SyncStateContract.Columns
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.rememberAsyncImagePainter
import com.example.shoppingcartcompose.Helper.ChangeNumberItemsListener
import com.example.shoppingcartcompose.Helper.ManagmentCart
import com.example.shoppingcartcompose.R
import com.example.shoppingcartcompose.model.ItemsModel

class CartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartScreen(
                ManagmentCart(this),
                onBackClick = {
                    finish()
                }
            )
        }
    }

    @Composable
    private fun CartScreen(
        managementCart: ManagmentCart = ManagmentCart(LocalContext.current),
        onBackClick: () -> Unit
    ) {
        val cartItems = remember {
            mutableStateOf(managementCart.getListCart())
        }
        val tax = remember {
            mutableStateOf(0.0)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier.padding(top = 36.dp)
            ) {
                val (backBtn, cartText) = createRefs()
                Text(
                    text = "Your Cart",
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(cartText) { centerTo(parent) },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Image(painter = painterResource(id = R.drawable.back), contentDescription = null,
                    modifier = Modifier
                        .clickable { onBackClick() }
                        .constrainAs(backBtn) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        })

            }
            if (cartItems.value.isEmpty()) {
                Text(
                    text = "Cart Is Empty",
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
            } else {
                CartList(
                    cartItems = cartItems.value, managementCart
                ) {
                    cartItems.value = managementCart.getListCart()
                    calculateCart(managementCart, tax)
                }
                CartSummary(
                    itemTotal = managementCart.getTotalFee(),
                    tax = tax.value,
                    delivery = 10.0,

                    )
            }
        }
    }

    @Composable
    fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
        val total = itemTotal + tax + delivery
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Item Total:",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.grey)
                )
                Text(text = "$$itemTotal")

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Tax:",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.grey)
                )
                Text(text = "$$tax")

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "Delivery:",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.grey)
                )
                Text(text = "$$delivery")

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        colorResource(id = R.color.grey)
                    )
                    .padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Total:",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.grey)
                )
                Text(text = "$$total")

            }
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple)),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Check Out",
                    fontSize = 18.sp,
                    color = Color.White
                )

            }

        }
    }

    private fun calculateCart(managementCart: ManagmentCart, tax: MutableState<Double>) {
        val percentTax = 0.02

        tax.value = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100.0
    }

    @Composable
    fun CartList(
        cartItems: ArrayList<ItemsModel>,
        managementCart: ManagmentCart,
        onItemChange: () -> Unit
    ) {
        LazyColumn(
            Modifier.padding(top = 16.dp)
        ) {
            items(cartItems) { item ->
                CartItem(
                    cartItems,
                    item = item,
                    managementCart = managementCart,
                    onItemChanged = onItemChange
                )
            }

        }
    }

    private @Composable
    fun CartItem(
        cartItems: ArrayList<ItemsModel>,
        item: ItemsModel,
        managementCart: ManagmentCart,
        onItemChanged: () -> Unit
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            val (pic, titleText, feeEachTime, totalEachItem, Quantity) = createRefs()
            Image(painter = rememberAsyncImagePainter(model = item.picUrl[0]),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        colorResource(id = R.color.lightGrey),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
                    .constrainAs(pic) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)

                    }
            )
            Text(text = item.title,
                modifier = Modifier
                    .constrainAs(titleText) {
                        start.linkTo(pic.end)
                        top.linkTo(pic.top)
                    }
                    .padding(start = 8.dp, top = 8.dp))
            Text(text = "$${item.price}", color = colorResource(id = R.color.purple),
                modifier = Modifier
                    .constrainAs(feeEachTime) {
                        start.linkTo(titleText.start)
                        top.linkTo(titleText.bottom)
                    }
                    .padding(start = 8.dp, top = 8.dp)
            )
            Text(text = "$${item.numberInCart * item.price}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold, modifier = Modifier
                    .constrainAs(totalEachItem) {
                        start.linkTo(titleText.start)
                        bottom.linkTo(pic.bottom)
                    }
                    .padding(start = 8.dp)
            )
            ConstraintLayout(modifier = Modifier
                .width(100.dp)
                .constrainAs(Quantity) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .background(
                    colorResource(id = R.color.lightGrey),
                    shape = RoundedCornerShape(10.dp)
                )
            ) {
                val (plusCartBtn, minusCartBtn, numberItemTxt) = createRefs()
                Text(text = item.numberInCart.toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(numberItemTxt) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
                Box(modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        colorResource(id = R.color.purple),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .constrainAs(plusCartBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managementCart.plusItem(
                            cartItems,
                            cartItems.indexOf(item),
                            object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChanged()
                                }

                            })
                    }) {
                    Text(
                        text = "+",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(28.dp)
                        .background(
                            colorResource(id = R.color.white),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .constrainAs(minusCartBtn) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .clickable {
                            managementCart.minusItem(cartItems,
                                cartItems.indexOf(item), object : ChangeNumberItemsListener {
                                    override fun onChanged() {
                                        onItemChanged()
                                    }

                                })
                        }
                ) {
                    Text(
                        text = "-",
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }


        }

    }

}
