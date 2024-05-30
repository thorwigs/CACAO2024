# AfriKakao

Description :
<br/><br/>
* Faire des bénéfices en assurant une vie équitable à tous les travailleurs. 
* Assurer un prix compétitif dans le marché. 
* Assurer un rapport social respectant les normes.

# L'équipe

* ER-RAHMAOUY Abderrahmane : Yuri69420

* GHARBI Haythem : Haythem7

* BEN ABDELJELIL Youssef : youssef7rouz

* ALAOUI-MRANI Fatim-ezzahra : fzaMM

### Les problèmes potentiels :

- **Surcharge Fonctionnelle :** Complexité excessive rendant la maintenance difficile.
- **Gestion des Exceptions :** Absence de gestion des exceptions.
- **Concurrence :** Problèmes potentiels en environnement multi-thread.
- **Gestion des Listes :** Complexité dans la gestion des listes comme `anciennete`.
- **Erreurs d'Index :** Risque d'erreurs de dépassement d'index dans `updateAnciennete`.
- **Valeurs Codées en Dur :** Réduction de la flexibilité et de l'adaptabilité.
- **Encapsulation :** Manipulation directe des champs, menant à un état incohérent.
- **Vérification d'Égalité :** Comportements inattendus si de nouveaux champs sont ajoutés.
- **Documentation :** Manque de documentation pour certaines méthodes.
- **Magic Numbers :** Forte dépendance aux valeurs dispersées dans le code.
- **Valeurs Aléatoires :** Gestion inefficace des valeurs aléatoires dans `effet_saison`.
- **Réglage des Terrains :** Inefficacités dans la logique de `adjustPlantationSize`.
- **Gestion des Stocks :** Gestion potentiellement incorrecte des stocks si la méthode `next` est interrompue.
- **Gestion de la degradations des stocks:** La degradation de stock ne prend pas en compte le label de la feve.
- **Calculs Redondants :** Redondance dans les méthodes `Stockage_*`.
- **Synchronisation :** Manque de synchronisation lors de l'accès aux ressources partagées comme `stock`.
- **Journalisation :** Redondance dans les messages de journalisation.
- **Mémoire :** Risque de dépassement des limites de mémoire avec une croissance non limitée dans des listes comme `bourseBQ`.
- **Structures Imbriquées :** Complexité des if-else réduisant la lisibilité.
- **Arithmétique :** Problèmes potentiels avec l'arithmétique en virgule flottante dans les calculs de prix.
- **Flux de Contrôle :** Difficulté à tracer le flux de contrôle en raison de multiples responsabilités dans une seule méthode.
- **Complexité de `proposerVente` :** Utilisation inefficace des structures de données pour la gestion des prix et des listes noires.
- **Évolutivité :** Manque d'évolutivité si le nombre d'offres augmente considérablement.
- **Erreurs Logiques :** Risque d'erreurs dans la détermination de la meilleure offre en raison de structures if-else imbriquées.
- **Cohérence des Enchères :** Manque de cohérence dans la gestion des différents types d'enchères.
