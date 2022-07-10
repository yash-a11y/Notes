package com.example.notes

import android.app.AlertDialog
import android.app.Dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager

import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.databinding.UpdatedialogBinding

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch



class MainActivity : AppCompatActivity() {

    private var bind : ActivityMainBinding ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        val dao = (application as notedbapp).DB.getnoteDao()
        setContentView(bind?.root)





        bind?.btnadd?.setOnClickListener {

                addnotes(dao)


            }

        lifecycleScope.launch {


            dao.fetchnotes().collect {

                val notelist = ArrayList(it)
                if (notelist !=null) {
                    setofallnotes(notelist, dao)
                }
            }

        }


        }



    private fun addnotes(dao: noteDao) {
        val noteinput = bind?.inputtxt?.text.toString()
        if(noteinput.isNotEmpty())
        {

            lifecycleScope.launch {

                dao.insertnote(notes_entity(note=noteinput))
                Toast.makeText(this@MainActivity, "note added", Toast.LENGTH_SHORT).show()
                bind?.inputtxt?.text!!.clear()
            }

         }
        else{
            Toast.makeText(this@MainActivity, "enter some lines", Toast.LENGTH_SHORT).show()
        }

}

    private fun setofallnotes(arrylsofnote : ArrayList<notes_entity>,dao: noteDao)
    {
        if(arrylsofnote.isNotEmpty())
        {
            bind?.recyclelay?.visibility = View.VISIBLE
            bind?.noRecordtxt?.visibility = View.INVISIBLE
            val adapter = noteadapter(arrylsofnote,

                {
                  updateId ->
                    updatenoterecored(updateId,dao)
                }

                ,
            {
                deleteID ->
                deletenote(deleteID,dao)
            }
        )
            bind?.recyclelay?.layoutManager = GridLayoutManager(this,2)
            bind?.recyclelay?.adapter = adapter
        }
        else
        {

            bind?.noRecordtxt?.visibility = View.VISIBLE
            bind?.recyclelay?.visibility = View.INVISIBLE
        }
    }

    private fun updatenoterecored(id : Int,dao: noteDao)
    {
        // setup dialog :

        val updatedialog = Dialog(this,R.style.Theme_dialog)
        val updatebind = UpdatedialogBinding.inflate(layoutInflater)

        updatedialog.setContentView(updatebind?.root)

        updatedialog.setCancelable(false)

        //get previous record from database using coroutines

        lifecycleScope.launch {

            dao.applyupdatenote(id).collect {
                if(it != null) {
                    updatebind?.updatetxt?.setText(it.note)
                }
            }

        }

        updatebind?.updatebtn?.setOnClickListener {

            val newnote : String = updatebind?.updatetxt?.text.toString()

            if(newnote.isNotEmpty())
            {
                lifecycleScope.launch {

                    dao.updatenote(notes_entity(id=id,note=newnote))

                    updatedialog.dismiss()
                }

            }
            else
            {
                Toast.makeText(this, "enter some lines", Toast.LENGTH_SHORT).show()
            }

//            Toast.makeText(this@MainActivity,"update button click",Toast.LENGTH_SHORT).show()
        }

        updatebind?.cancelbtn?.setOnClickListener{

            updatedialog.dismiss()
        }

        updatedialog.show()

    }

    private fun deletenote(id : Int,dao: noteDao)
    {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Are you sure you want delete it")
        builder.setIcon(R.drawable.warning)

        builder.setPositiveButton("Yes")
        { dialogInterface,_ ->

            lifecycleScope.launch {

                        dao.deletenote(notes_entity(id))
            }
            Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No"){
            dialogInterface,_ ->

            dialogInterface.dismiss()
        }

        val alertdialog : AlertDialog = builder.create()
        alertdialog.setCancelable(false)

        alertdialog.show()

    }
}