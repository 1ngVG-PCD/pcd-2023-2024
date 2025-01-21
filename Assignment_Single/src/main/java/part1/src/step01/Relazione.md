
# 1. Analisi del problema

> **Obbiettivo:** Dato un indirizzo di directory `D` e una parola `P`, contare quanti file PDF contengono `P`.
> 
>**Task:** Ogni task dovrà eseguire la lettura di un file PDF per cercare la parola.
> 
>**Decomposizione dei dati:** I dati da processare sono i file PDF presenti nelle sottodirectory.
>
> **Dipendenze:** Le operazioni sui singoli file sono indipendenti tra loro, il che consente di parallelizzare senza conflitti sui dati.


# 2. Scelta dell’architettura concorrente

>**Architettura suggerita:** "Master-Worker"
> 
> -Un thread *"master"* coordina il lavoro.
>
> -I thread *"worker"* leggono i file e cercano la parola chiave.
>
> -Una *bag of tasks* conterrà i file da processare.


# 3. Design dettagliato

## **Componenti attivi e passivi**
> **Componente attivo:** Un *"worker"* che esegue la ricerca nel file.
>
> **Componente passivo:** Una struttura condivisa (*monitor*) per accodare i file da leggere e sincronizzare l’accesso ai risultati.

## **Uso dei monitor**
> I worker accedono a un monitor per sincronizzarsi ed evitare conflitti sull’incremento del conteggio totale dei file trovati.


# 4. Implementazione passo-passo

## **a. Struttura del progetto**
> **Main:** Crea il pool di thread e avvia la scansione della directory.
>
> **Worker:** Classe thread che legge i file PDF e cerca la parola `P`.
>
> **Monitor:** Contiene una coda dei file e mantiene il conteggio dei risultati.


# 5. Test e verifica

> Controlla casi limite (directory vuote, nessun file PDF).
>
> Testa con PDF di diverse dimensioni.
>
> Assicurati di gestire correttamente eventuali eccezioni.
