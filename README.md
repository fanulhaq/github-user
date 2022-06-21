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

Sekarang kita memiliki 3 module yang diperlukan, setalah itu ayo kita pergi menuju kelas *App* dan *AndroidManifest*. Kita akan melakukan hal sederhana untuk mengaktifkan injeksi.

*App*
```
@HiltAndroidApp
class App: Application()
```

*AndroidManifest* tambahkan -> *android:name=".App"*
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

Setelah kita mengaktifkan injeksi, kita dapat mulai mengaktifkan injeksi anggota di kelas Android yang lain menggunakan anotasi *@AndroidEntryPoint*. Kita dapat menggunakan *@AndroidEntryPoint* pada jenis berikut:
1. Activity
2. Fragment
3. View
4. Service
5. BroadcastReceiver

Sedangkan untuk ViewModel dapat menggunakan *@HiltViewModel*.

## Build Interface
1. Antarmuka terdiri dari Fragment *SearchFragment* dan file tata letaknya *fragment_search.xml*.
2. Untuk berinteraksi dengan antarmuka memerlukan model data yang menyimpan elemen data *List<SearchModel>* dan *State*.

Pada project ini kita akan menggunakan *SearchVM* (ViewModel) untuk menyimpan informasi/data.

```
@HiltViewModel
class SearchVM @Inject constructor(): BaseViewModel() {
    ...
    private var _search = MutableLiveData<Resource<List<SearchModel>>>()
    val search: LiveData<Resource<List<SearchModel>>>
        get() = _search

    ...
}
```

Pada *SearchVM* kita melihat *Resource*, kelas tersebut digunakan untuk memberitahu state yang sedang dan atau sudah dijalankan.

```
sealed class Resource<T> {

    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T, val finishLoading: Boolean) : Resource<T>()
    data class Error<T>(val message: String, val code: Int) : Resource<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T, finishLoading: Boolean) = Success(data, finishLoading)
        fun <T> error(message: String, code: Int) = Error<T>(message, code)
    }
}
```

Kemudian lihat *SearchFragment*
  
```
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
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
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
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

## Cache & Retain Data

Saat ini, ada banyak cara untuk mengimplementasikan caching, seperti OkHttp CacheControl atau RxCache, tetapi dalam proyek ini, kita akan menggunakan database, anggota JetPack lainnya, *Room*.

Untuk menggunakan Room, kita perlu mendefinisikan skema lokal. Pertama, kita tambahkan anotasi *@Entity* ke kelas model data *SearchModel* dan anotasi *@PrimaryKey* ke bidang id kelas. Anotasi ini akan menandai *SearchModel* sebagai tabel di database dan id sebagai Primary Key untuk tabel tersebut:

```
@Entity(tableName = "search")
data class SearchModel (
    @PrimaryKey 
    val id: Int,
    val username: String,
    val avatar: String,
    val url: String
)
```

Kemudian, kita membuat kelas database dengan mengimplementasikan RoomDatabase untuk aplikasi:

```
@Database(entities = [SearchModel::class], version = 1)
abstract class RoomDB: RoomDatabase()
```

Sekarang, kita membutuhkan cara untuk melakukan Create, Read, Update, dan Delete (CRUD) pada database. Untuk mencapai ini, kita akan membuat Data Access Object (DAO).

```
@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<SearchModel>)

    @Query("SELECT * FROM search WHERE username LIKE '%' || :key || '%' LIMIT 30")
    fun getDataWithKey(key: String): Flow<List<SearchModel>>

    @Query("DELETE FROM search WHERE username LIKE '%' || :key || '%'")
    suspend fun deleteWithKey(key: String)
}
```

Setelah mendefinisikan kelas *SearchDao*, referensikan DAO dari kelas database:

```
@Database(entities = [SearchModel::class], version = 1)
abstract class RoomDB: RoomDatabase() {
  abstract fun searchDao(): SearchDao
}
```

## Data Collection
Sekarang kita telah menghubungkan *SearchVM* ke *SearchFragment* menggunakan LiveData dan sudah memiliki database, bagaimana kita mendapatkan data?

Dalam project ini menggunakan Retrofit
  
```
interface GithubApi {
    ...
    @GET("search/users")
    suspend fun search(@Query("q") q: String): Response<SearchResponse<SearchModel>>
    ...
}
```

Ide pertama untuk mengimplementasikan ViewModel, mungkin dengan memanggil *GithubApi* secara langsung untuk mendapatkan data dan kemudian menetapkan data tersebut ke objek LiveData. Desain ini berfungsi, tetapi dengan desain ini seiring berkembangnya aplikasi, aplikasi menjadi semakin sulit untuk dipelihara. Ini akan menempatkan terlalu banyak tanggung jawab pada kelas *SearchVM*, yang akan melanggar prinsip pemisahan masalah.

*SearchVM* akan mendelegasikan proses pengambilan data ke modul lain. Jadi, sekarang kita membuat kelas repositori yang terdiri dari *SearchRepo* dan *SearchRepoImpl*. 
  
```
interface SearchRepo {
    suspend fun search(q: String) : Flow<Resource<List<SearchModel>>>
}
```
  
```
@ExperimentalCoroutinesApi
class SearchRepoImpl @Inject constructor(
    private val githubApi: GithubApi,
    private val searchDao: SearchDao
) : SearchRepo {
    override suspend fun search(q: String): Flow<Resource<List<SearchModel>>> {
        return object : ResourceBound<List<SearchModel>, SearchResponse<SearchModel>>() {
            override suspend fun saveRemoteData(response: SearchResponse<SearchModel>) = response.items?.let { searchDao.insertAll(it) }
            override suspend fun fetchFromRemote(): Response<SearchResponse<SearchModel>> = githubApi.search(q = q)
            override suspend fun deleteData() = searchDao.deleteWithKey(q)
            override fun fetchFromLocal(): Flow<List<SearchModel>> = searchDao.getDataWithKey(q)
        }.asFlow()
    }
}
```

Pada *SearchRepoImpl* terdapat abstract class *ResourceBound*, digunakan untuk memperbarui state dan mengontrol aliran data yang kita ambil. *SearchRepoImpl* merupakan sebuah repositori yang menyediakan sumber data dari database lokal serta remote endpoint.
Mari lihat *ResourceBound*.

```
@ExperimentalCoroutinesApi
abstract class ResourceBound<RESULT, REQUEST> {

    fun asFlow() = flow {

        emit(Resource.loading())
        emit(
            Resource.success(
                fetchFromLocal().first(),
                false)
        )

        val apiResponse = fetchFromRemote()
        val remoteBody = apiResponse.body()

        if(apiResponse.isSuccessful && remoteBody != null) {
            deleteData()
            saveRemoteData(remoteBody)
        } else {
            emit(Resource.error(apiResponse.message(), apiResponse.code()))
        }

        emitAll(
            fetchFromLocal().map {
                Resource.success(it, true)
            }
        )
    }.catch { e ->
        emit(Resource.error(e.message ?: "Unknown Error", -1))
        e.printStackTrace()
    }


    @WorkerThread
    protected abstract suspend fun deleteData()

    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST): Unit?

    @MainThread
    protected abstract fun fetchFromLocal(): Flow<RESULT>

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}
```

*RESULT* mewakili model data dari database dan *REQUEST* mewakili model data response dari service/network.

Meskipun modul repositori mungkin tampak tidak perlu, ia memiliki tujuan penting: ia menarik sumber data dari aplikasi lainnya.  
Sekarang, *SearchVM* tidak tahu cara mendapatkan data, jadi kita dapat menyediakan model tampilan dengan data yang diperoleh dari beberapa implementasi pengambilan data yang berbeda.
 
## Connecting ViewModel to Repository
Mari kembali ke *SearchVM*.  
  
```  
@HiltViewModel
@ExperimentalCoroutinesApi
class SearchVM @Inject constructor(
    private val repository: SearchRepoImpl
): BaseViewModel() {

    private var _search = MutableLiveData<Resource<List<SearchModel>>>()
    val search: LiveData<Resource<List<SearchModel>>>
        get() = _search

    fun search(q: String) {
        viewModelScope.launch {
            repository.search(q).collect {
                _search.postValue(it)
            }
        }
    }
}
```  
  
*SearchFragment* memperbarui UI dengan mengamati LiveData<Resource<List<SearchModel>>> di *SearchVM* dan memuat data dengan memanggil *search()*.

Diatas adalah contoh kecil untuk mendapatkan data pada project ini.  
  
## Paging  
Paging adalah komponen yang ditetapkan oleh Google untuk memudahkan pengembang menyelesaikan pemuatan paging. Paging mendukung tiga jenis skema, yaitu data Network, Database, data Naringan + Database.

Pada project ini menggunakan Paging3 karena dapat mendeteksi status daftar data secara realtime, dan juga menyediakan mekanisme refresh dan retry, dll.

Selanjutnya, mengambil daftar data repositori user pada Fragment *DetailFragment*. Sebagai contoh untuk menggambarkan implementasi Paging.
Pertama adapter PagingDataAdapter, kita perlu inherit PagingDataAdapter pada kelas adapter kita. Seperti pada *ReposAdapter*.  
  
```  
class ReposAdapter @Inject constructor() : PagingDataAdapter<ReposModel, ReposAdapter.ViewHolder>(ReposComparator) {
    ...
    object ReposComparator : DiffUtil.ItemCallback<ReposModel>() {
        override fun areItemsTheSame(oldItem: ReposModel, newItem: ReposModel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ReposModel, newItem: ReposModel) = oldItem == newItem
    }
    ...
}
```
  
Pada project ini, menerapkan paging pemuatan data Network + Database, kita perlu menggunakan kelas RemoteMediator di Paging3. Sebagai referensi, mari kita lihat kelas *ReposMediator*. 
  
```  
@ExperimentalPagingApi
class ReposMediator(
    private val service: GithubApi,
    private val db: RoomDB,
    private val username: String
) : RemoteMediator<Int, ReposModel>() {

    private val reposDao = db.reposDao()
    private val pageKeyDao = db.pageKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ReposModel>
    ): MediatorResult {
        return try {
            var page = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    getRemoteKey(state)?.next ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = remoteData(page)
            response?.map {
                it.username = username
                it.star = it.star?.toLong()?.numberShortFormatter()
                it.updated_at = it.updated_at?.countDateUpdate()
            }
            val endOfPaginationReached = response?.size ?: 0 < NETWORK_PAGE_SIZE

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pageKeyDao.deleteWithUsername(username)
                    reposDao.deleteWithUsername(username)
                }
                val keys = response?.map {
                    PageKey(
                        id = it.id,
                        username = username,
                        prev = null /**Only paging forward*/,
                        next = if(endOfPaginationReached) null else (page+1))
                }
                keys?.let { pageKeyDao.insertAll(it) }
                response?.let { reposDao.insertAll(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKey(state: PagingState<Int, ReposModel>) : PageKey? {
        val lastItem = state.lastItemOrNull()
        return db.withTransaction {
            if(lastItem?.id != null) {
                pageKeyDao.getRemoteKeyWithUsername(lastItem.id, username)
            } else pageKeyDao.getLastRemoteKeyWithUsername(username)
        }
    }

    private suspend fun remoteData(page: Int): List<ReposModel>? {
        return withContext(Dispatchers.IO) {
            val response = async { service.repos(username = username, page = page) }
            if (response.await().isSuccessful) {
                response.await().body()
            } else throw HttpException(response.await())
        }
    }

}  
```  
  
Diatas adalah operasi sederhana dari paging pemuatan data Network + Database di *ReposMediator*. Kita bisa mendapatkan status melalui LoadType (REFRESH, PREPEND, APPEND). Fungsinya adalah jika database tidak kosong, paging juga dapat menampilkan data saat offline. Jika kosong dan offline, akan ditampilkan pesan kesalahan. Kemudian jika online, akan mendapatkan data dari network, setelah berhasil. Kemudian menghapus database lokal dan masukkan data terbaru. Disini, terdapat tabel database yang digunakan untuk menyimpan nomor *(PageKey)* halaman yang dimuat (Only paging forward).


Kemudian panggil juga di *DetailRepoImpl* sebagai berikut.

```
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class DetailRepoImpl @Inject constructor(
    private val githubApi: GithubApi,
    private val reposDao: ReposDao,
    private var db: RoomDB
) : DetailRepo {
  
    override suspend fun repos(username: String): Flow<PagingData<ReposModel>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, prefetchDistance = 2*NETWORK_PAGE_SIZE),
        remoteMediator = ReposMediator(githubApi, db, username)
    ) {
        reposDao.pagingSourceWithUsername(username)
    }.flow
}
```


*Give Feedback* 😉


