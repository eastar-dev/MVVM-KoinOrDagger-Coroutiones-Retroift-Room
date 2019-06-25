package dummy

import android.log.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.eastar.branch.data.BranchDatabase
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BranchDBUnitTest {

    /*
     * Inject needed components from Koin
     */
//    val db: BranchDatabase by inject()
//    val dao: BranchDao by inject()
//    val source: BranchDBSource by inject()

    /**
     * Override default Koin configuration to use Room in-memory database
     */
    @Before
    fun before() {
        Log.MODE = Log.eMODE.SYSTEMOUT
        Log.e("startBranchKoin")
//        startBranchKoin {
//            androidLogger()
//            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
//            roomModule
//        }
        Log.w("startBranchKoin")
        DB생성()
    }

    fun `DB생성`() {
        Log.e("DB생성")
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = Room.inMemoryDatabaseBuilder(context, BranchDatabase::class.java).build()
        val plantDao = database.branchDao()

//        db.branchDao()
        Log.e("DB생성")
    }
}