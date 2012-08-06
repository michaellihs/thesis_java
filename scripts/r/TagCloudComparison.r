#! /usr/bin/env Rscript

###############################################################
#
# This script reads some files with tag cloud similarities (one depth
# at each line) and draws a plot depth -> tag cloud similarities
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
normalizedSetDifferenceWithStopWords <- scan(args[1])
normalizedSetDifferenceWithStopWords <- as.numeric(normalizedSetDifferenceWithStopWords)

normalizedTopNSetDifference <- scan(args[2])
normalizedTopNSetDifference <- as.numeric(normalizedTopNSetDifference)

normalizedSetDifference <- scan(args[3])
normalizedSetDifference <- as.numeric(normalizedSetDifference)

setDifference <- scan(args[4])
setDifference <- as.numeric(setDifference)

# Open up a new pdf file for output given as cmd-line argument 2
pdf(args[5])

plot(normalizedSetDifferenceWithStopWords, xlab="Depth", ylab="normalized difference", type="l", col="red")
lines(normalizedTopNSetDifference, type="l", col="blue")
lines(normalizedSetDifference, type="l", col="green")

par(new=TRUE) 
plot(setDifference, axes=FALSE, col="orange", type="l", ylab='', xlab='') 
axis(4) 
mtext("set difference",4) 

dev.off() 

# Quit execution of r
q()