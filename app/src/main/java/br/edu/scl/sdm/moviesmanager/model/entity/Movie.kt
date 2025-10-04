package br.edu.scl.sdm.moviesmanager.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity
data class Movie (
    @PrimaryKey
    val name: String,
    val year: Int,
    val studio: String,
    val minutes: Int,
    var watched: Boolean = false,
    var score: Double? = null,
    val genre: String
):Parcelable{

}





