# TP Android Studio — Calcul de l'IMC

Application Android (Kotlin, interface XML) qui demande le poids et la taille, calcule l'indice de masse corporelle et affiche la valeur avec une interprétation colorée.

| Projet | Langage | Modèle | SDK min |
|---|---|---|---|
| CalculIMC | Kotlin | Empty Views Activity | API 24 |

## Fonctionnalités

- Saisie du **poids (kg)** et de la **taille (m)** avec clavier numérique décimal (la virgule est acceptée).
- Formule : `IMC = poids / (taille × taille)`, arrondie à **deux décimales**.
- Catégorie déterminée par `if / else if / else` et couleur adaptée (orange, vert, rouge, rouge foncé).
- Bouton **Effacer** : vide les champs, supprime le résultat, replace le curseur dans le poids.
- Gestion des erreurs : champ vide, valeur nulle ou négative, nombre invalide (message dans le champ + Toast). L'application ne se ferme jamais.

Améliorations facultatives : résultat **conservé après rotation**, bouton **Partager le résultat**, taille saisie en **centimètres** convertie automatiquement (valeur > 3).

## Fichiers principaux

- [`activity_main.xml`](app/src/main/res/layout/activity_main.xml) — ids `editTextPoids`, `editTextTaille`, `buttonCalculer`, `buttonEffacer`, `textViewImc`, `textViewCategorie`
- [`MainActivity.kt`](app/src/main/java/com/example/calculimc/MainActivity.kt)
- [`strings.xml`](app/src/main/res/values/strings.xml)

## Ouvrir et lancer

Cloner le dépôt, **File > Open** dans Android Studio, attendre la synchronisation Gradle puis ▶ **Run**.

## Tests réalisés

| Données | Attendu | Obtenu | Capture |
|---|---|---|---|
| 70 kg, 1,75 m | 22,86 — Corpulence normale | ✅ | [02](captures/02_corpulence_normale.png) |
| 50 kg, 1,75 m | 16,33 — Insuffisance pondérale | ✅ | [03](captures/03_insuffisance_ponderale.png) |
| 85 kg, 1,70 m | 29,41 — Surpoids | ✅ | [04](captures/04_surpoids.png) |
| 100 kg, 1,70 m | 34,60 — Obésité modérée | ✅ | [05](captures/05_obesite_moderee.png) |
| Champ vide | aucun calcul, message | ✅ | [06](captures/06_erreur_champ_vide.png) |
| Taille = 0 | aucun calcul, message | ✅ | [07](captures/07_erreur_taille_zero.png) |

<p>
<img src="captures/02_corpulence_normale.png" width="200"> <img src="captures/05_obesite_moderee.png" width="200"> <img src="captures/07_erreur_taille_zero.png" width="200">
</p>

## Compte rendu

[`compte-rendu/compte_rendu.md`](compte-rendu/compte_rendu.md) — PDF : [`compte_rendu.pdf`](compte-rendu/compte_rendu.pdf)

> L'IMC est utilisé ici comme support de programmation. Son interprétation ne remplace pas un avis médical.
