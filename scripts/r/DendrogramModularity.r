#! /usr/bin/env Rscript

###############################################################
#
# This script reads a file with modularities divided by \n
# and plots a digram with those measures
#
# Run this script with (first command line argument is modularity file (txt), second command line argument is path to pdf file)
# $ ./DendrogramModularity.r results/dendrogramModularities.txt plots/dendrogramModularities.pdf
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

# Read file with modularities
modularities <- scan(args[1])
modularities <- as.numeric(modularities)

# Open up a new pdf file for output given as cmd-line argument 2
pdf(args[2])

plot(modularities, xlab="Merge Step", ylab="Modularity", col='blue', type="l")
# legend("topleft", c("cluster size 1 merge","cluster size 2 merges","cluster size 4 merges", "cluster size 8 merges"), col=c("blue","red","green","orange"), lty=c(1,1,1,1), lwd=c(2,2,2,2))

dev.off() 

# Quit execution of r
q()