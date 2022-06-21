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


## Module Dependency Injection
Disini kita akan membuat 3 Module diantaranya *NetworkModule, DBModule & RepositoryModule*.

1. *NewtworkModule* berisi sesuatu yang berhubungan dengan Service/Network.
```
@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    ...
    @Provides
    @Singleton
    @Named("RetrofitGithub")
    fun provideRetrofitGithub(
        gson: Gson,
        @Named("OkHttpClient") okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder().apply {
            baseUrl("...")
            addConverterFactory(GsonConverterFactory.create(gson))
            client(okHttpClient)
       }.build()

    @Provides
    @Singleton
    fun provideGithubApi(@Named("RetrofitGithub") retrofit: Retrofit):
            GithubApi = retrofit.create(GithubApi::class.java)
    ....
}
```

2. *DBModule* untuk inject dababase.
```
@InstallIn(SingletonComponent::class)
@Module
class DBModule {
    ...
    @Singleton
    @Provides
    fun provideDatabase(application: Application) = Room.databaseBuilder(
        application,
        RoomDB::class.java,
        "..."
    )
    .fallbackToDestructiveMigration()
    .build()

    @Singleton
    @Provides
    fun provideSearchDao(db: RoomDB) = db.searchDao()
    ...
}
```

3. *RepositoryModule* untuk inject semua repositori yang akan kita gunakan.
```
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    ...
    @Binds
    abstract fun bindSearchRepository(searchRepoImpl: SearchRepoImpl): SearchRepo
    ...
}
```

Sekarang kita memiliki 3 module yang diperlukan, setalah itu ayo kita pergi menuju kelas *App* dan *AndroidManifest*. Kita akan melakukan hal penting agar Dependency Injection kita tidak mengalami masalah.

*App*
```
@HiltAndroidApp
class App: Application() {
    ...
    ...
}
```

*AndroidManifest* tambahkan baris android:name=".App"
```
...
<application
      android:name=".App"
      android:allowBackup="true"
      android:icon="@mipmap/ic_launcher"
      android:label="@string/app_name"
      android:roundIcon="@mipmap/ic_launcher_round"
      android:supportsRtl="true"
      android:usesCleartextTraffic="true"
      android:theme="@style/Theme.GithubUser"
      tools:ignore="UnusedAttribute">
      ...
      ...
</application>
...
```


## Build Interface
1. Antarmuka terdiri dari Fragment *SearchFragment* dan file tata letaknya *fragment_search.xml*.
2. Untuk berinteraksi dengan antarmuka memerlukan model data yang menyimpan elemen data *List<SearchModel>* dan *State*.

Pada project ini kita akan menggunakan *SearchVM* (ViewModel) untuk menyimpan informasi/data.

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

*LiveData* adalah penyimpanan data yang dapat diamati. Komponen lain dalam aplikasi dapat menggunakan penyimpanan ini untuk memantau perubahan pada objek tanpa membuat jalur ketergantungan yang eksplisit dan rumit di antara mereka. Komponen LiveData juga mengikuti status siklus hidup komponen aplikasi seperti Aktivitas, Fragmen, dan Service, serta menyertakan logika pembersihan untuk mencegah kebocoran objek dan konsumsi memori yang berlebihan.
Saat ini kita sudah menggunakan *LiveData<Resource<List<SearchModel>>>* di *SearchVM*. *SearchFragment* sekarang diberi tahu saat data diperbarui. Selain itu, karena *LiveData* ini sadar akan siklus hidup, referensi secara otomatis dibersihkan saat tidak lagi diperlukan.

Sekarang, kita berada di *SearchFragment* untuk mengamati data dan memperbarui antarmuka:

```
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search), 
  OnSearchListener {
    ...
    private val viewModel: SearchVM by viewModels()
  
    private fun subscribeToObservables() = with(binding) {
        viewModel.search.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    progressBar.isVisible = true
                    searchAdapter.clearAdapter()
                }
                is Resource.Success -> {
                    if(state.finishLoading) progressBar.isVisible = false
                    if(!state.data.isNullOrEmpty()) {
                        searchAdapter.addAll(state.data as ArrayList<SearchModel>, true)
                    }
                }
                is Resource.Error -> {
                    progressBar.isVisible = false
                    context.toast(state.message.ifEmpty { "Error ${state.code}" })
                }
            }
        }
    }
    ...
}
```


  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
