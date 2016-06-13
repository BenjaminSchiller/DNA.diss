package dna.diss.algorithms;

import java.io.IOException;

import argList.ArgList;
import argList.types.array.EnumArrayArg;
import argList.types.atomic.IntArg;
import argList.types.atomic.StringArg;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.util.ReadableFileGraph;
import dna.metrics.Metric;
import dna.metrics.assortativity.AssortativityR;
import dna.metrics.assortativity.AssortativityU;
import dna.metrics.degree.DegreeDistributionR;
import dna.metrics.degree.DegreeDistributionU;
import dna.metrics.motifs.UndirectedMotifsU;
import dna.metrics.richClub.RichClubConnectivityByDegreeR;
import dna.metrics.richClub.RichClubConnectivityByDegreeU;
import dna.plot.Plotting;
import dna.plot.PlottingConfig.PlotFlag;
import dna.series.Series;
import dna.series.data.SeriesData;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.util.ReadableDirConsecutiveBatchGenerator;
import dna.util.Config;

public class Evaluation {

	public GraphGenerator gg;
	public BatchGenerator bg;

	public Metric[] metrics;

	public String dataDir;
	public int batches;
	public int runs;

	public String plotDir;
	public PlotFlag[] plotFlags;

	public static enum EvalType {
		DD, W, RCC, ASS, M
	}

	public Evaluation(String name, String datasetDir, String graphFilename,
			String batchSuffix, String[] evalTypes, String dataDir,
			Integer batches, Integer runs, String plotDir, String[] plotFlags)
			throws IOException {
		this.gg = new ReadableFileGraph(datasetDir, graphFilename);
		this.bg = new ReadableDirConsecutiveBatchGenerator(name, datasetDir,
				batchSuffix);

		this.metrics = parse(evalTypes);

		this.dataDir = dataDir;
		this.batches = batches;
		this.runs = runs;

		this.plotDir = plotDir;
		this.plotFlags = new PlotFlag[plotFlags.length];
		for (int i = 0; i < plotFlags.length; i++) {
			this.plotFlags[i] = PlotFlag.valueOf(plotFlags[i]);
		}
	}

	public static Metric[] parse(String[] types) {
		Metric[] metrics = new Metric[types.length * 2];
		for (int i = 0; i < types.length; i++) {
			EvalType t = EvalType.valueOf(types[i]);
			switch (t) {
			case ASS:
				metrics[i * 2] = new AssortativityR();
				metrics[i * 2 + 1] = new AssortativityU();
				break;
			case DD:
				metrics[i * 2] = new DegreeDistributionR();
				metrics[i * 2 + 1] = new DegreeDistributionU();
				break;
			case M:
				metrics[i * 2] = new UndirectedMotifsU();
				metrics[i * 2 + 1] = new UndirectedMotifsU();
				break;
			case RCC:
				metrics[i * 2] = new RichClubConnectivityByDegreeR();
				metrics[i * 2 + 1] = new RichClubConnectivityByDegreeU();
				break;
			case W:
				metrics[i * 2] = null;
				metrics[i * 2 + 1] = null;
			default:
				throw new IllegalArgumentException("invalid eval type: " + t);
			}
		}
		return metrics;
	}

	public static void main(String[] args) throws Exception {
		ArgList<Evaluation> argList = new ArgList<Evaluation>(
				Evaluation.class,
				new StringArg("name", "name of the dataset"),
				new StringArg("datasetDir", "dir where the dataset is stored"),
				new StringArg("graphFilename", "e.g., 0.dnag"),
				new StringArg("batchSuffix", "e.g., '.dnab'"),
				new EnumArrayArg("evalType", "types of metrics to evaluate",
						",", EvalType.values()),
				new StringArg("dataDir",
						"dir where the results should be stored"),
				new IntArg("batches", "# of batches to analyze"),
				new IntArg("runs",
						"# of runs to execute (0 for single run without aggregation)"),
				new StringArg("plotDir",
						"dir where the plots should be stored (use - to diable plotting)"),
				new EnumArrayArg("plotFlag", "what should be plotted", ",",
						PlotFlag.values()));

		Evaluation e = argList.getInstance(args);
		e.evaluate();
	}

	public void evaluate() throws Exception {
		Config.zipRuns();
		Series s = new Series(gg, bg, metrics, dataDir, bg.getName());
		SeriesData sd = (runs == 0) ? s.generateRuns(0, 0, batches) : s
				.generate(runs, batches);
		if (!plotDir.equals("-")) {
			Plotting.plot(sd, plotDir, plotFlags);
		}
	}
}
