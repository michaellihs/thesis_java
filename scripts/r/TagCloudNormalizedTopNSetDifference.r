#! /usr/bin/env Rscript

###############################################################
#
# This script reads a file with tag cloud similarities (one depth
# at each line) and draws a plot depth -> tag cloud similarity
#
# Run this script with
# $ ./TagCloudNormalizedTopNSetDifference.r /.../inputFile.txt /.../output.pdf
#
# This script is part of Michael Knoll's Diploma Thesis
# (c) Michael Knoll, 2012
#
###############################################################

library("igraph")
library("Rlab") # For xline command

# Make this script executeable and call it via ./ScriptName.R
# You can add arbitrary command line arguments which will then be read via
args <- commandArgs(TRUE)

# Read file with mergeStep -> clusterSize tuples from file
similarities <- scan(args[1])
similarities <- as.numeric(similarities)

# Open up a new pdf file for output given as cmd-line argument 2
pdf(args[2])

plot(similarities, xlab="Depth", type="l", col="red", ylab=args[3])
dev.off() 

# Quit execution of r
q()