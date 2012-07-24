#! /usr/bin/env Rscript

###############################################################
#
# This script reads a file with 
#
# clusterSize_at_step1 \t clusterSize_at_step2 \t clusterSize_at_step4 \t clusterSize_at_step8
#
# and plots a digram with those measures
#
# Run this script with (first command line argument is step size file (txt), second command line argument is path to pdf file)
# $ ./NodeStepClusterSize.r /Users/mimi/Dropbox/Diplomarbeit/Tests/R/nodeStepAtClusterSize.txt /Users/mimi/Dropbox/Diplomarbeit/Tests/R/nodeStepAtClusterSize.pdf
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
clusterSizes <- read.table(args[1])

# Open up a new pdf file for output given as cmd-line argument 2
pdf(args[2])

#clusterSizes
clusterSizes1 <- as.numeric(clusterSizes[[1]])
#mode(clusterSizes)
clusterSizes1 <- clusterSizes1[order(clusterSizes1)]
#clusterSizes1

clusterSizes2 <- as.numeric(clusterSizes[[2]])
clusterSizes2 <- clusterSizes2[order(clusterSizes2)]

clusterSizes4 <- as.numeric(clusterSizes[[3]])
clusterSizes4 <- clusterSizes4[order(clusterSizes4)]

clusterSizes8 <- as.numeric(clusterSizes[[4]])
clusterSizes8 <- clusterSizes8[order(clusterSizes8)]

plot(clusterSizes1, xlab="Node", ylab="Cluster Size", col='blue', type="l", log="y")
lines(clusterSizes2, col='red')
lines(clusterSizes4, col='green')
lines(clusterSizes8, col='orange')
yline(2, col="blue")
yline(4, col="red")
yline(16, col="green")
yline(256, col="orange")
legend("topleft", c("cluster size 1 merge","cluster size 2 merges","cluster size 4 merges", "cluster size 8 merges"), col=c("blue","red","green","orange"), lty=c(1,1,1,1), lwd=c(2,2,2,2))

dev.off() 

# Quit execution of r
q()