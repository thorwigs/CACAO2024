Productor 3 ti'Cao

Ti'cao A côté


arthurl7389 
Gabin32
MAMMOUYoussef
alexisrenaudin

PROBLEMES DANS LE CODE:

-pour les négociations d'échéanciers de CC, on prend en compte la quantité disponible (production - quantités à livrer sur les autres CC) sur chaque step du CC. Malgré cela, des livraisons ne sont pas toujours honorées (surement a cause d'un décalage de step). Pour régler le problème, on regarde le minimum de la valeur au step avant et après de chaque step (on camoufle le problème quitte a vendre moins)


