# Compte rendu — TP Android : calcul de l'IMC

**Nom et prénom :** …………………… **Groupe :** ……………

## 1. Objectif

Développer une application qui lit le poids et la taille, calcule l'IMC, affiche la valeur et une interprétation, et contrôle les saisies.

## 2. Interface

Un `ScrollView` (utile sur petit écran ou en paysage) contient un `LinearLayout` vertical :

- titre « Calcul de l'IMC » ;
- deux `EditText` en `inputType="numberDecimal"` avec les indications *Poids en kg* et *Taille en m* ;
- les boutons **Calculer** et **Effacer** côte à côte (poids 1/1) ;
- `textViewImc` (36sp) et `textViewCategorie` (22sp) pour le résultat ;
- un bouton **Partager** affiché seulement après un calcul valide.

## 3. Logique de calcul (`MainActivity.kt`)

1. Lecture et `trim()` des deux champs.
2. Si un champ est vide : `editText.error` + Toast, aucun calcul.
3. Conversion avec `toDoubleOrNull()` après remplacement de la virgule par un point : une saisie invalide ne provoque pas d'exception.
4. Contrôle `poids > 0` et `taille > 0`.
5. Si la taille dépasse 3, elle est considérée en centimètres et divisée par 100.
6. `imc = round(poids / (taille * taille) * 100) / 100`.
7. Catégorie par `if / else if / else` sur les seuils 18,5 – 25 – 30 – 35 – 40, qui renvoie le texte et la couleur.
8. Affichage formaté avec `String.format(Locale.FRANCE, "%.2f", imc)` → « 22,86 ».

**Effacer** vide les champs et les erreurs, efface le résultat et appelle `editTextPoids.requestFocus()`.

## 4. Tests

| Données | Attendu | Obtenu |
|---|---|---|
| 70 / 1,75 | 22,86 Corpulence normale | ✅ |
| 50 / 1,75 | 16,33 Insuffisance pondérale | ✅ |
| 85 / 1,70 | 29,41 Surpoids | ✅ |
| 100 / 1,70 | 34,60 Obésité modérée | ✅ |
| Champ vide | message d'erreur | ✅ |
| Taille 0 | message d'erreur | ✅ |
| 70 / 175 (cm) | 22,86 | ✅ |

<p><img src="../captures/02_corpulence_normale.png" width="180"> <img src="../captures/06_erreur_champ_vide.png" width="180"> <img src="../captures/07_erreur_taille_zero.png" width="180"></p>

## 5. Difficultés rencontrées

- **Séparateur décimal** : selon la langue du clavier, l'utilisateur tape une virgule ; `toDouble()` échouait. Le texte est normalisé (`,` → `.`) et converti avec `toDoubleOrNull()`.
- **Arrondi et affichage** : l'arrondi est fait sur la valeur numérique, puis l'affichage utilise `Locale.FRANCE` pour obtenir la virgule.
- **Ancien résultat après une erreur** : un résultat précédent restait affiché après une saisie incorrecte ; il est désormais effacé à chaque erreur.
- **Rotation** : le dernier IMC est sauvegardé dans `onSaveInstanceState` puis réaffiché.
