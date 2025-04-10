package com.debk007.template.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debk007.template.R
import com.debk007.template.presentation.item.DailyStockItem
import com.debk007.template.presentation.viewmodel.HomeViewModel
import com.debk007.template.util.UiState

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val dailyStockItemsState by homeViewModel.dailyStockItemsState.collectAsState()

    when (dailyStockItemsState) {

        is UiState.Loading -> if ((dailyStockItemsState as UiState.Loading).isLoading) {
            LoadingLayout()
        }

        is UiState.Error -> {
            Toast.makeText(
                LocalContext.current,
                (dailyStockItemsState as UiState.Error).errorMsg,
                Toast.LENGTH_SHORT
            ).show()
        }

        is UiState.Success -> {
            val dailyStockItems =
                (dailyStockItemsState as UiState.Success<List<DailyStockItem>>).data

            Column(
                modifier = Modifier.padding(6.dp)
            ) {
                var isBottomSheetShown by remember { mutableStateOf(false) }

                if (isBottomSheetShown) {
                    BottomSheetDialog(
                        onDismissRequest = { isBottomSheetShown = false },
                        onDescendingClick = {
                            homeViewModel.setSortStatus(HomeViewModel.SortType.Descending)
                            isBottomSheetShown = false
                        },
                        onAscendingClick = {
                            homeViewModel.setSortStatus(HomeViewModel.SortType.Ascending)
                            isBottomSheetShown = false
                        }
                    )
                }

                IconButton(
                    onClick = { isBottomSheetShown = true },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                DailyStockItems(
                    list = dailyStockItems
                )
            }
        }
    }
}

@Composable
fun LoadingLayout() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(32.dp))
    }
}

@Composable
fun DailyStockItems(
    list: List<DailyStockItem>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = list, key = { it.code }) { stock ->
                DailyStockItem(item = stock)
            }
        }
    }
}

@Composable
fun DailyStockItem(item: DailyStockItem, modifier: Modifier = Modifier) {
    var isDialogShown by remember { mutableStateOf(false) }

    if (isDialogShown) {
        Dialog(
            onDismissRequest = { isDialogShown = false },
            onConfirmClick = { isDialogShown = false },
            peRatio = item.peRatio,
            dividendYield = item.dividendYield,
            pbRatio = item.pbRatio
        )
    }

    ElevatedCard(
        onClick = { isDialogShown = true },
        modifier = modifier
            .wrapContentSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(
                top = 6.dp
            )
        ) {
            Text(
                text = "(${item.code})",
                modifier = Modifier.padding(
                    horizontal = 4.dp
                ),
                fontSize = 12.sp
            )
            Text(
                text = "(${item.name})",
                modifier = Modifier.padding(
                    start = 4.dp,
                    bottom = 6.dp
                ),
                fontSize = 20.sp,
            )
            Row(
                modifier = Modifier.padding(
                    horizontal = 6.dp,
                    vertical = 2.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.opening_price),
                    modifier = Modifier
                        .width(width = 90.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 14.sp,
                    textAlign = TextAlign.End
                )
                Text(
                    text = "(${item.openingPrice})",
                    modifier = Modifier
                        .width(width = 100.dp),
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
                Text(
                    text = stringResource(R.string.closing_price),
                    modifier = Modifier
                        .width(width = 80.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 14.sp,
                    textAlign = TextAlign.End
                )

                val closingPriceColor = when {
                    item.closingPrice > item.avgPriceMonthly -> Color.Red
                    item.closingPrice < item.avgPriceMonthly -> colorResource(R.color.dark_green)

                    else -> Color.Unspecified
                }

                Text(
                    text = "(${item.closingPrice})",
                    modifier = Modifier
                        .width(width = 100.dp),
                    color = closingPriceColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
            Row(
                modifier = Modifier.padding(
                    horizontal = 6.dp,
                    vertical = 2.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.highest_price),
                    modifier = Modifier
                        .width(width = 90.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 14.sp,
                    textAlign = TextAlign.End
                )
                Text(
                    text = "(${item.highestPrice})",
                    modifier = Modifier
                        .width(width = 100.dp),
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
                Text(
                    text = stringResource(R.string.lowest_price),
                    modifier = Modifier
                        .width(width = 80.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 14.sp,
                    textAlign = TextAlign.End
                )
                Text(
                    text = "(${item.lowestPrice})",
                    modifier = Modifier
                        .width(width = 100.dp),
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
            Row(
                modifier = Modifier.padding(
                    horizontal = 6.dp,
                    vertical = 2.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.spread),
                    modifier = Modifier
                        .width(width = 90.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 14.sp,
                    textAlign = TextAlign.End
                )

                val changeColor = when {
                    item.change > 0 -> Color.Red
                    item.change < 0 -> colorResource(R.color.dark_green)

                    else -> Color.Unspecified
                }

                Text(
                    text = "(${item.change})",
                    modifier = Modifier
                        .width(width = 100.dp),
                    color = changeColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
                Text(
                    text = stringResource(R.string.average_monthly_price),
                    modifier = Modifier
                        .width(width = 80.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 14.sp,
                    textAlign = TextAlign.End
                )
                Text(
                    text = "(${item.avgPriceMonthly})",
                    modifier = Modifier
                        .width(width = 100.dp),
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = 1.dp,
                        vertical = 6.dp
                    )
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.number_of_transactions),
                    modifier = Modifier
                        .wrapContentSize(),
                    fontSize = 13.sp
                )
                Text(
                    text = "(${item.transaction})",
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            start = 2.dp
                        ),
                    fontSize = 13.sp
                )
                Text(
                    text = stringResource(R.string.number_of_shares_traded),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            start = 6.dp
                        ),
                    fontSize = 13.sp
                )
                Text(
                    text = "(${item.tradeVolume})",
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            start = 2.dp
                        ),
                    fontSize = 13.sp
                )
                Text(
                    text = stringResource(R.string.transaction_amount),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            start = 6.dp
                        ),
                    fontSize = 13.sp
                )
                Text(
                    text = "(${item.tradeValue})",
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            start = 2.dp
                        ),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog(
    onDismissRequest: () -> Unit,
    onDescendingClick: () -> Unit,
    onAscendingClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        TextButton(
            onClick = { onDescendingClick() },
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.sort_in_descending_order),
                color = Color.Black,
                fontSize = 18.sp
            )
        }
        TextButton(
            onClick = { onAscendingClick() },
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.sort_in_ascending_order),
                color = Color.Black,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    peRatio: String,
    dividendYield: String,
    pbRatio: String
) {
    AlertDialog(
        text = {
            Text(text = stringResource(R.string.dialog_message, peRatio, dividendYield, pbRatio))
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = { onConfirmClick() }
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    )
}