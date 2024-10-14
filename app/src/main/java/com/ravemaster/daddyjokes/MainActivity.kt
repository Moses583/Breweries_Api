package com.ravemaster.daddyjokes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ravemaster.daddyjokes.data.BreweriesRepositoryImplementation
import com.ravemaster.daddyjokes.data.RetrofitInstance
import com.ravemaster.daddyjokes.data.models.Breweries
import com.ravemaster.daddyjokes.data.models.Brewery
import com.ravemaster.daddyjokes.presentation.BreweriesViewModel
import com.ravemaster.daddyjokes.ui.theme.DaddyJokesTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<BreweriesViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BreweriesViewModel(BreweriesRepositoryImplementation(RetrofitInstance.getBrews))
                        as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaddyJokesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val breweriesList = viewModel.breweries.collectAsState().value
                    val context = LocalContext.current

                    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
                        viewModel.showErrorToastChannel.collectLatest { show ->
                            if (show) {
                                Toast.makeText(
                                    context,
                                    "Unable to fetch breweries",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }
                    }
                    if (breweriesList.isEmpty()) {
                        ShowProgressIndicator(modifier = Modifier.padding(innerPadding))
                    } else {
                        ShowLazyColumn(modifier = Modifier.padding(innerPadding),breweriesList)
                    }

                }
            }
        }
    }
}

@Composable
fun ShowProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ShowLazyColumn(
    modifier: Modifier = Modifier,
    breweries: Breweries
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(breweries.size){item ->
            ShowBreweryItem(breweries[item], modifier)
        }
    }
}

@Composable
fun ShowBreweryItem(brewery: Brewery, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.inverseSurface)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = modifier.padding(bottom = 10.dp),
                text = brewery.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inverseOnSurface)
            Text(
                modifier = modifier.padding(bottom = 10.dp),
                text = brewery.city,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.inverseOnSurface)
            Text(
                modifier = modifier.padding(bottom = 10.dp),
                text = brewery.country,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.inverseOnSurface)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DaddyJokesTheme {

    }
}