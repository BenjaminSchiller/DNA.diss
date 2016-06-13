# DNA.diss

This repo contains evaluation code for DNA used in my diss.

## general directory structure

	build/ (ant build file)
	eval/ (script for deployment, analysis, and evaluation)
	java/ (java sources)


## datasets

For the generation of datasets, we use the scripts provided [here](https://github.com/BenjaminSchiller/DNA.datasets).

### datasets - directory structure

	datasets/
		name/
			


## evaluation

For the evaluation, the evaluation.jar file is provided.
It takes the following arguments:

	expecting 10 arguments (got 0)
	   0: name - name of the dataset (String)
	   1: datasetDir - dir where the dataset is stored (String)
	   2: graphFilename - e.g., 0.dnag (String)
	   3: batchSuffix - e.g., '.dnab' (String)
	   4: evalType - types of metrics to evaluate (String[]) sep. by ','
	      from: DD W RCC ASS M
	   5: dataDir - dir where the results should be stored (String)
	   6: batches - # of batches to analyze (Integer)
	   7: runs - # of runs to execute (0 for single run without aggregation) (Integer)
	   8: plotDir - dir where the plots should be stored (use - to diable plotting) (String)
	   9: plotFlag - what should be plotted (String[]) sep. by ','
	      from: plotAll plotStatistics plotRuntimes plotMetricValues plotMetricEntirely plotDistributions plotNodeValueLists plotCustomValues plotSingleScalarValues plotMultiScalarValues


### evaluation - directory structure

	data/
		$dataset/
			$evalTypes/
				$batches-$runs/
	plots/
		$dataset/
			$evalTypes/
				$batches-$runs/