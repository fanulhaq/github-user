# Github-User


- 100% [Kotlin](https://kotlinlang.org/) 
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
- Architecture
  - MVVM Architecture (Model - View - ViewModel - Model)
  - Repository Pattern
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency Injection
  - SSOT
- JetPack
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Notify domain layer data to views
  - [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - Dispose observing data when lifecycle state changes
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - UI related data holder, lifecycle aware
  - [Room](https://developer.android.com/topic/libraries/architecture/room) Persistence - Construct database
  - [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - Display data on recycleView
  - [Navigation Component](https://developer.android.com/guide/navigation) - Support for single Activity 
- Material Design
- [Retrofit2 & Gson](https://github.com/square/retrofit) - Constructing the REST API
- [OkHttp3](https://github.com/square/okhttp) - Implementing interceptor, logging and mocking web server
- [Glide](https://github.com/bumptech/glide) - Loading images


## Dependency Injection


## Build Interface
1. Antarmuka terdiri dari Fragment *SearchFragment* dan file tata letaknya *fragment_search.xml*.
2. Untuk berinteraksi dengan antarmuka memerlukan model data yang menyimpan elemen data *List<SearchModel>* dan *State*.

Kami akan menggunakan *SearchVM* (ViewModel) untuk menyimpan informasi tersebut.

```
@HiltViewModel
@ExperimentalCoroutinesApi
class SearchVM @Inject constructor(
    private val repository: SearchRepoImpl
): BaseViewModel() {
    ...
    private var _search = MutableLiveData<Resource<List<SearchModel>>>()
    val search: LiveData<Resource<List<SearchModel>>>
        get() = _search

    ...
}
```

Kemudian lihat *SearchFragment*
  
```
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search), 
  OnSearchListener {
    ...
    private val viewModel: SearchVM by viewModels()
    ...
}
```
  
Sekarang setelah kita memiliki *SearchVM* dan *SearchFragment*, bagaimana cara menghubungkannya?, untuk mendapatkan data di kelas *SearchVM*, kita memerlukan cara untuk memberi tahu antarmuka. Pada saat ini kita memerlukan anggota JetPack lainnya yaitu *LiveData*.  
  
  
  
  
  
  
