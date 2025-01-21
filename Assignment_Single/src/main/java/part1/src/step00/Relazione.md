# 1. Analisi del problema

> **Obbiettivo:** Dato un indirizzo di directory `D` e una parola `P`, contare quanti file PDF contengono `P`.
> 
> **Modalità:** In questa fase si approccerà al problema in modo sequenziale,
>               per comprende l'entità del problema e capire le funzionalità necessarie ad una soluzione "banale".

# 2. Design dettagliato

> **Main:** Gestisce la lettura dei dati dal terminale (D e P)
> 
> **Services:** Package contenente codice necessario e instanziabile da più classi.
> 
> **SeqSearch:** Classe che risolve il problema in modo sequenziale.

# 3. Note 
> Apache PDFBox non è in grado di mappare un carattere specifico ("trianglerightsld") 
> nella font incorporata BFLQCD+MSAM10 a un equivalente Unicode.
> Per la risoluzione di questo problema si possono utilizzare librerie OCR,
> tuttavia allo stato attuale manterrò il codice in forma base.
