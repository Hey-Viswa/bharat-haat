package com.optivus.bharat_haat.ui.components.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckoutSummary(
    cartItems: List<CheckoutItem>,
    subtotal: String,
    shippingCost: String,
    tax: String,
    total: String,
    modifier: Modifier = Modifier,

    // Optional components
    discountCode: String? = null,
    discountAmount: String? = null,
    onApplyCoupon: (String) -> Unit = {},

    // Actions
    onQuantityChange: (String, Int) -> Unit = { _, _ -> },
    onRemoveItem: (String) -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Order Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Cart items
            LazyColumn(
                modifier = Modifier.heightIn(max = 200.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { item ->
                    CheckoutItemRow(
                        item = item,
                        onQuantityChange = { newQuantity ->
                            onQuantityChange(item.id, newQuantity)
                        },
                        onRemove = { onRemoveItem(item.id) }
                    )
                }
            }

            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Coupon section
            CouponSection(
                discountCode = discountCode,
                discountAmount = discountAmount,
                onApplyCoupon = onApplyCoupon
            )

            // Price breakdown
            PriceBreakdown(
                subtotal = subtotal,
                shippingCost = shippingCost,
                tax = tax,
                total = total,
                discountAmount = discountAmount
            )
        }
    }
}

@Composable
private fun CheckoutItemRow(
    item: CheckoutItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            if (item.variant.isNotEmpty()) {
                Text(
                    text = item.variant,
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
            Text(
                text = item.price,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9800)
            )
        }

        // Quantity controls
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Decrease",
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = item.quantity.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(
                onClick = { onQuantityChange(item.quantity + 1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Increase",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Remove",
                tint = Color(0xFFE53E3E),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun CouponSection(
    discountCode: String?,
    discountAmount: String?,
    onApplyCoupon: (String) -> Unit
) {
    var couponCode by remember { mutableStateOf("") }

    Column {
        Text(
            text = "Promo Code",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (discountCode == null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = couponCode,
                    onValueChange = { couponCode = it },
                    placeholder = { Text("Enter coupon code") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (couponCode.isNotEmpty()) {
                            onApplyCoupon(couponCode)
                        }
                    },
                    enabled = couponCode.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800)
                    )
                ) {
                    Text("Apply")
                }
            }
        } else {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE8F5E8),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Coupon Applied: $discountCode",
                            fontSize = 12.sp,
                            color = Color(0xFF2E7D32)
                        )
                    }

                    if (discountAmount != null) {
                        Text(
                            text = "-$discountAmount",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PriceBreakdown(
    subtotal: String,
    shippingCost: String,
    tax: String,
    total: String,
    discountAmount: String?
) {
    Column {
        PriceRow("Subtotal", subtotal)
        PriceRow("Shipping", shippingCost)
        PriceRow("Tax", tax)

        if (discountAmount != null) {
            PriceRow("Discount", "-$discountAmount", Color(0xFF4CAF50))
        }

        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Text(
                text = total,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9800)
            )
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    amount: String,
    amountColor: Color = Color(0xFF666666)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )
        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = amountColor
        )
    }
}

@Composable
fun ShippingAddressForm(
    address: ShippingAddress,
    onAddressChange: (ShippingAddress) -> Unit,
    modifier: Modifier = Modifier,
    isEditable: Boolean = true
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Shipping Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Full Name
            OutlinedTextField(
                value = address.fullName,
                onValueChange = { onAddressChange(address.copy(fullName = it)) },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditable,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Phone Number
            OutlinedTextField(
                value = address.phoneNumber,
                onValueChange = { onAddressChange(address.copy(phoneNumber = it)) },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditable,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Address Line 1
            OutlinedTextField(
                value = address.addressLine1,
                onValueChange = { onAddressChange(address.copy(addressLine1 = it)) },
                label = { Text("Address Line 1") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditable,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Address Line 2
            OutlinedTextField(
                value = address.addressLine2,
                onValueChange = { onAddressChange(address.copy(addressLine2 = it)) },
                label = { Text("Address Line 2 (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditable,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // City and Postal Code
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = address.city,
                    onValueChange = { onAddressChange(address.copy(city = it)) },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f),
                    enabled = isEditable,
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = address.postalCode,
                    onValueChange = { onAddressChange(address.copy(postalCode = it)) },
                    label = { Text("PIN Code") },
                    modifier = Modifier.weight(1f),
                    enabled = isEditable,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // State
            OutlinedTextField(
                value = address.state,
                onValueChange = { onAddressChange(address.copy(state = it)) },
                label = { Text("State") },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditable,
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun PaymentMethodSelector(
    paymentMethods: List<PaymentMethod>,
    selectedMethod: PaymentMethod?,
    onMethodSelect: (PaymentMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Payment Method",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            paymentMethods.forEach { method ->
                PaymentMethodItem(
                    method = method,
                    isSelected = method == selectedMethod,
                    onSelect = { onMethodSelect(method) }
                )

                if (method != paymentMethods.last()) {
                    HorizontalDivider(
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF9800))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            imageVector = method.icon,
            contentDescription = null,
            tint = Color(0xFF666666),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = method.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            if (method.description.isNotEmpty()) {
                Text(
                    text = method.description,
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (method.isEnabled) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Available",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = "Not Available",
                fontSize = 12.sp,
                color = Color(0xFFE53E3E)
            )
        }
    }
}

@Composable
fun OrderReviewSection(
    orderDetails: OrderDetails,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Order Review",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Shipping Address Section
            OrderInfoSection(
                title = "Shipping Address",
                content = {
                    Text(
                        text = "${orderDetails.shippingAddress.fullName}\n" +
                                "${orderDetails.shippingAddress.addressLine1}\n" +
                                "${orderDetails.shippingAddress.city}, ${orderDetails.shippingAddress.state} ${orderDetails.shippingAddress.postalCode}",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
            )

            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Payment Method Section
            OrderInfoSection(
                title = "Payment Method",
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = orderDetails.paymentMethod.icon,
                            contentDescription = null,
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = orderDetails.paymentMethod.name,
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            )

            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Estimated Delivery
            OrderInfoSection(
                title = "Estimated Delivery",
                content = {
                    Text(
                        text = orderDetails.estimatedDelivery,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4CAF50)
                    )
                }
            )
        }
    }
}

@Composable
private fun OrderInfoSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

// Data classes
data class CheckoutItem(
    val id: String,
    val name: String,
    val variant: String,
    val price: String,
    val quantity: Int
)

data class ShippingAddress(
    val fullName: String = "",
    val phoneNumber: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = ""
)

data class PaymentMethod(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val isEnabled: Boolean = true
)

data class OrderDetails(
    val shippingAddress: ShippingAddress,
    val paymentMethod: PaymentMethod,
    val estimatedDelivery: String
)

@Preview(showBackground = true)
@Composable
private fun CheckoutPreviews() {
    val sampleItems = listOf(
        CheckoutItem("1", "Wireless Headphones", "Black, Medium", "₹1,999", 2),
        CheckoutItem("2", "Smartphone Case", "Blue", "₹599", 1)
    )

    val sampleAddress = ShippingAddress(
        fullName = "John Doe",
        phoneNumber = "+91 9876543210",
        addressLine1 = "123 Main Street",
        city = "Mumbai",
        state = "Maharashtra",
        postalCode = "400001"
    )

    val paymentMethods = listOf(
        PaymentMethod("upi", "UPI Payment", "Pay using UPI apps", Icons.Default.Payment),
        PaymentMethod("card", "Credit/Debit Card", "Visa, Mastercard, Rupay", Icons.Default.CreditCard),
        PaymentMethod("cod", "Cash on Delivery", "Pay when you receive", Icons.Default.LocalShipping)
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CheckoutSummary(
            cartItems = sampleItems,
            subtotal = "₹2,598",
            shippingCost = "Free",
            tax = "₹260",
            total = "₹2,858"
        )

        ShippingAddressForm(
            address = sampleAddress,
            onAddressChange = { }
        )

        PaymentMethodSelector(
            paymentMethods = paymentMethods,
            selectedMethod = paymentMethods.first(),
            onMethodSelect = { }
        )
    }
}
