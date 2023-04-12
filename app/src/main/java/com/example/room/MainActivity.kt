package com.example.room

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.room.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding: ActivityMainBinding
    private var database: DatabaseManager? = null
    private var executorService: ExecutorService? = null
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnInsertData.setOnClickListener(this)
        binding.btnRetrieveData.setOnClickListener(this)
        binding.btnUpdateData.setOnClickListener(this)
        binding.btnDeleteData.setOnClickListener(this)

        database = DatabaseManager.getInstance(this)
        executorService = Executors.newSingleThreadExecutor()
        handler = Handler(Looper.getMainLooper())
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnInsertData -> insert()
            R.id.btnRetrieveData -> retrieve()
            R.id.btnUpdateData -> update()
            R.id.btnDeleteData -> delete()
        }
    }

    private fun delete() {
        val isDeleted = booleanArrayOf(false)
        executorService!!.execute {
            //task
            var userToBeDeleted: UserModel? = null
            val nameToBeDeleted: String = binding.tilDataToBeDeleted.editText?.text.toString()
            for (i in database!!.userDao().allUsers!!.indices) {
                if (database!!.userDao().allUsers!![i]!!.name == nameToBeDeleted) {
                    userToBeDeleted = database!!.userDao().allUsers!![i]
                    break
                }
            }
            if (userToBeDeleted != null) {
                database!!.userDao().deleteUser(userToBeDeleted)
                isDeleted[0] = true
            }
            handler!!.post { //update ui
                if (isDeleted[0]) {
                    Toast.makeText(
                        this@MainActivity, "Data deleted.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity, "Operation failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun update() {
        val isUpdated = booleanArrayOf(false)
        executorService!!.execute {
            //task
            val oldName: String = binding.tilDataToBeUpdated.editText?.text.toString()
            val newName: String = binding.tilUpdatedData.editText?.text.toString()
            var id = -1
            for (i in database!!.userDao().allUsers!!.indices) {
                if (database!!.userDao().allUsers!![i]!!.name == oldName) {
                    id = database!!.userDao().allUsers!![i]!!.id
                    break
                }
            }
            val userModel = database!!.userDao().getUser(id)
            if (userModel != null) {
                userModel.name = newName
                database!!.userDao().updateUser(userModel)
                isUpdated[0] = true
            }
            handler!!.post { //update ui
                if (isUpdated[0]) {
                    Toast.makeText(
                        this@MainActivity, "Data updated.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity, "Operation failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun retrieve() {
        executorService!!.execute {
            //task
            val allUsers = database!!.userDao().allUsers
            handler!!.post { //update ui
                Toast.makeText(
                    this@MainActivity, allUsers.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun insert() {
        val isInserted = booleanArrayOf(false)
        executorService!!.execute { //task
            val name: String = binding.tilInsert.editText?.text.toString()
            database!!.userDao().insert(UserModel(name))
            for (i in database!!.userDao().allUsers!!.indices) {
                if (database!!.userDao().allUsers!![i]!!.name == name) {
                    isInserted[0] = true
                }
            }
            handler!!.post {
                if (isInserted[0]) {
                    Toast.makeText(
                        this@MainActivity, "Data inserted.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity, "Operation failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}