#!/bin/bash

source jobs.cfg

rsync -auvzl config.cfg dataset.sh evaluation.sh ../../DNA.datasets/build/dna-dataset.jar ../build/evaluation.jar $server_name:$server_dir/
