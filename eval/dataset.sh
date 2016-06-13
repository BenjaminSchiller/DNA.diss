#!/bin/bash

if [[ "$#" != "8" ]]; then
	echo 'expecting 8 arguments:' >&2
	echo '    dataset.sh $gdsType $gdsArguments $graphType $graphArguments $batchType $batchArguments $seed $batches' >&2
	exit
fi

source config.cfg

gdsType=$1
gdsArguments=$2
graphType=$3
graphArguments=$4
batchType=$5
batchArguments=$6
seed=$7
batches=$8

destDir=$datasetDir/${gdsType}____${gdsArguments//,/__}/${graphType}____${graphArguments//,/__}/${batchType}____${batchArguments//,/__}/${seed}____${batches}/

if [[ -d $destDir ]]; then
	echo "$destDir exists already, skipping"
else
	java -Xms1g -Xmx8g -jar dna-dataset.jar $gdsType $gdsArguments $graphType $graphArguments $batchType $batchArguments $seed $batches $destDir $graphFilename $batchSuffix
fi
