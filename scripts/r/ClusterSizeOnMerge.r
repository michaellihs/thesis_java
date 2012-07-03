#! /usr/bin/env Rscript

###############################################################
#
# This script reads a file with step -> clusterSize 
# tuples from a file given as first command line argument
# and draws a plot joinStep(as line) -> clusterSize into
# 
# Format of file is one cluster size per line, e.g. 
# a file given as second command line argument
#  
# 1
# 1
# 2
# ...
#
# Run this script with
# $ ./ClusterSizeOnMerge.r /Users/mimi/Dropbox/Diplomarbeit/Tests/R/merge_size.txt /Users/mimi/Dropbox/Diplomarbeit/Tests/R/output.pdf
#
# This script is part of Michael Knoll's Diploma Thesis
# (c) Michael Knoll, 2012
#
###############################################################

# Make this script executeable and call it via ./ScriptName.R
# You can add arbitrary command line arguments which will then be read via
args <- commandArgs(TRUE)

# Read file with mergeStep -> clusterSize tuples from file
clusterSizes <- scan(args[1], what="numeric")

# Open up a new pdf file for output given as cmd-line argument 2
pdf(args[2], 7, 5)

plot(clusterSizes, xlab="Merge Step", ylab="Cluster Size")
dev.off() 

# Quit execution of r
q()