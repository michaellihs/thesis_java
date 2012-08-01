#! /usr/bin/env Rscript

###############################################################
#
# This script reads a file with dendrogramDepth (one depth
# at each line) and draws a plot node -> depthForNode
#
# Run this script with
# $ ./DendrogramDepthPerNode.r /.../inputFile.txt /.../output.pdf
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
dendrogramDepthPerNode <- scan(args[1])
dendrogramDepthPerNode <- as.numeric(dendrogramDepthPerNode)
dendrogramDepthPerNode <- dendrogramDepthPerNode[order(dendrogramDepthPerNode)]

# Open up a new pdf file for output given as cmd-line argument 2
pdf(args[2])

dendrogramDepthPerNode
length(dendrogramDepthPerNode)
log2(length(dendrogramDepthPerNode))

plot(dendrogramDepthPerNode, xlab="Node", type="l", col="red", ylab="Dendrogram Depth for Node")
yline(log2(length(dendrogramDepthPerNode)), col="red")
dev.off() 

# Quit execution of r
q()