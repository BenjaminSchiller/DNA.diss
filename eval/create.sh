#!/bin/bash

function dataset {
	./jobs.sh create "./dataset.sh $1 $2 $3 $4 $5 $6 $7 $8"
}

dataset Undirected - Random 100,1000 Random 0,0,10,0 0 20