package com.cesoft.organizate2.repo.db

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.cesoft.organizate2.util.extension.None
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.ArrayList

/**
 * Created by ccasanova on 25/05/2018
 */
@RunWith(AndroidJUnit4::class)
class TaskTableInstrumentedTest {

    //@Rule
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var mDatabase: Database? = null
    private var dao: TaskDao? = null

    @Before
    @Throws(Exception::class)
    fun initDb() {
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), Database::class.java)
                .allowMainThreadQueries()
                .build()
        dao = mDatabase!!.dao()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        mDatabase!!.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun onFetchingNotes_shouldGetEmptyList_IfTable_IsEmpty() {
        val tasks = dao!!.selectRedux()
        assertTrue(tasks.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun onInsertingNotes_checkIf_RowCountIsCorrect() {
        val list1 = getFakeTasks(5)
        list1.forEach { task -> dao!!.insert(task) }
        val list2 = dao!!.selectRedux()

        assertEquals(list1.size, list2.size)
        val list3 = list1.map { task -> task.toTaskEntity() }
        //list1.forEach { task -> assertEquals(task.toTaskEntity(), list2.size) }
        //System.err.println(list3)
        assertTrue(list3.toTypedArray() contentDeepEquals list2.toTypedArray())
        assertEquals(list3, list2)
    }
    fun getFakeTasks(size: Int): List<TaskTable> {
        val noteList = ArrayList<TaskTable>()
        for(i in 1..size) {
            val note = TaskTable(i, Int.None, "Title $i", "Desc $i",
                    5, Int.None, System.currentTimeMillis()+10*60*60*1000,
                    System.currentTimeMillis(), System.currentTimeMillis())
            noteList.add(note)
        }
        return noteList
    }

/*
    @Test
    @Throws(InterruptedException::class)
    fun onUpdatingANote_checkIf_UpdateHappensCorrectly() {
        val note = FakeNotesSource.fetchFakeNote()
        notesDao!!.insert(note)
        note.setNoteTitle(FakeNotesSource.FAKE_NOTE_UPDATED_TITLE)
        notesDao!!.update(note)
        assertEquals(1, LiveDataTestUtil.getValue(notesDao!!.getAllNotes()).size())
        assertEquals(FakeNotesSource.FAKE_NOTE_UPDATED_TITLE,
                LiveDataTestUtil.getValue(notesDao!!.getNoteById(note.getId())).getNoteTitle())
    }

    @Test
    @Throws(InterruptedException::class)
    fun onNoteDeletion_CheckIf_NoteIsDeletedFromTable() {
        val noteList = FakeNotesSource.getFakeNotes(5)
        noteList.forEach { note -> notesDao!!.insert(note) }
        notesDao!!.deleteNote(noteList.get(2))
        assertNull(LiveDataTestUtil.getValue(notesDao!!.getNoteById(noteList.get(2).getId())))
    }
*/
}