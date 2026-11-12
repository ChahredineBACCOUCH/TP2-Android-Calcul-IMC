package com.example.calculimc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPoids: EditText
    private lateinit var editTextTaille: EditText
    private lateinit var textViewImc: TextView
    private lateinit var textViewCategorie: TextView
    private lateinit var buttonPartager: Button

    /** Dernier IMC calculé (null tant qu'aucun calcul valide n'a été fait). */
    private var dernierImc: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val barres = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(barres.left, barres.top, barres.right, barres.bottom)
            insets
        }

        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        textViewImc = findViewById(R.id.textViewImc)
        textViewCategorie = findViewById(R.id.textViewCategorie)
        buttonPartager = findViewById(R.id.buttonPartager)

        findViewById<Button>(R.id.buttonCalculer).setOnClickListener { calculer() }
        findViewById<Button>(R.id.buttonEffacer).setOnClickListener { effacer() }
        buttonPartager.setOnClickListener { partager() }

        // Conservation du dernier résultat après la rotation de l'écran
        if (savedInstanceState != null && savedInstanceState.containsKey(CLE_IMC)) {
            afficherResultat(savedInstanceState.getDouble(CLE_IMC))
        }
    }

    /** Lit les champs, valide les saisies, calcule l'IMC puis affiche le résultat. */
    private fun calculer() {
        val textePoids = editTextPoids.text.toString().trim()
        val texteTaille = editTextTaille.text.toString().trim()

        // 1. Les deux champs doivent être renseignés
        var complet = true
        if (textePoids.isEmpty()) {
            editTextPoids.error = getString(R.string.erreur_champ_vide); complet = false
        }
        if (texteTaille.isEmpty()) {
            editTextTaille.error = getString(R.string.erreur_champ_vide); complet = false
        }
        if (!complet) {
            effacerResultat()
            Toast.makeText(this, R.string.message_completer, Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Conversion en nombres décimaux (la virgule est acceptée)
        val poids = lireNombre(editTextPoids, textePoids)
        var taille = lireNombre(editTextTaille, texteTaille)
        if (poids == null || taille == null) { effacerResultat(); return }

        // 3. Les valeurs doivent être strictement positives
        if (poids <= 0.0 || taille <= 0.0) {
            if (poids <= 0.0) editTextPoids.error = getString(R.string.erreur_valeur_invalide)
            if (taille <= 0.0) editTextTaille.error = getString(R.string.erreur_valeur_invalide)
            effacerResultat()
            Toast.makeText(this, R.string.erreur_valeur_invalide, Toast.LENGTH_SHORT).show()
            return
        }

        // Une taille supérieure à 3 est considérée comme saisie en centimètres
        if (taille > 3.0) taille /= 100.0

        // 4. Formule : IMC = poids / (taille × taille), arrondi à deux décimales
        val imc = round(poids / (taille * taille) * 100) / 100
        afficherResultat(imc)
    }

    /** Convertit le texte en Double ; affiche une erreur dans le champ si la conversion échoue. */
    private fun lireNombre(champ: EditText, texte: String): Double? {
        val nombre = texte.replace(',', '.').toDoubleOrNull()
        if (nombre == null) champ.error = getString(R.string.erreur_nombre)
        return nombre
    }

    /** Affiche la valeur (deux décimales), la catégorie et la couleur correspondante. */
    private fun afficherResultat(imc: Double) {
        dernierImc = imc
        val (categorie, couleur) = categorie(imc)
        val couleurInt = ContextCompat.getColor(this, couleur)
        textViewImc.text = getString(R.string.libelle_imc, formater(imc))
        textViewImc.setTextColor(couleurInt)
        textViewCategorie.setText(categorie)
        textViewCategorie.setTextColor(couleurInt)
        buttonPartager.visibility = View.VISIBLE
    }

    /** Détermine la catégorie (texte, couleur) avec une structure if / else if / else. */
    private fun categorie(imc: Double): Pair<Int, Int> {
        return if (imc < 18.5) {
            R.string.categorie_insuffisance to R.color.imc_orange
        } else if (imc < 25) {
            R.string.categorie_normale to R.color.imc_vert
        } else if (imc < 30) {
            R.string.categorie_surpoids to R.color.imc_orange
        } else if (imc < 35) {
            R.string.categorie_obesite_moderee to R.color.imc_rouge
        } else if (imc < 40) {
            R.string.categorie_obesite_severe to R.color.imc_rouge
        } else {
            R.string.categorie_obesite_morbide to R.color.imc_rouge_fonce
        }
    }

    private fun formater(imc: Double) = String.format(Locale.FRANCE, "%.2f", imc)

    /** Supprime l'ancien résultat affiché. */
    private fun effacerResultat() {
        dernierImc = null
        textViewImc.text = ""
        textViewCategorie.text = ""
        buttonPartager.visibility = View.GONE
    }

    /** Vide les champs, supprime le résultat et replace le curseur dans le champ du poids. */
    private fun effacer() {
        editTextPoids.text.clear()
        editTextTaille.text.clear()
        editTextPoids.error = null
        editTextTaille.error = null
        effacerResultat()
        editTextPoids.requestFocus()
    }

    /** Partage le résultat avec une autre application (amélioration facultative). */
    private fun partager() {
        val imc = dernierImc ?: return
        val texte = getString(R.string.texte_partage, formater(imc), getString(categorie(imc).first))
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, texte)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.titre_partage)))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dernierImc?.let { outState.putDouble(CLE_IMC, it) }
    }

    companion object {
        private const val CLE_IMC = "dernier_imc"
    }
}
