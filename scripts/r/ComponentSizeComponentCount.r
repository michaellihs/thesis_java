#! /usr/bin/env Rscript

###############################################################
#
# This script reads an edgelist of a cluster and plots
# a diagram of component sizes and counts.
#
# Run this script with
# $ ./ComponentSizeComponentCount.r /.../edgelistFile /.../output.pdf
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






#################################################################
# Reading graph from edgelist file
#################################################################

 ## Reads edges of a graph into 'edges' from given file
 ## Edges have to be encoded as numeric IDs of integer separated by space
edges <- read.table(args[1], header=FALSE)
 ## Create undirected graph from edges
g <- graph.data.frame(edges, directed = F)



#################################################################
# Some statistics on the overall graph
#################################################################

 ## Calculate average degree
overallAverageDegree <- 2 * length(E(g)) / length(V(g))
 ## Number of nodes with degree n
table(degree(g, mode="out"))
 ## Number of clusters with size n
table(clusters(g)["csize"])



#################################################################
# Some statistics on the biggest component of the graph
#################################################################

 ## Get biggest component
maxComponentSize <- length(cluster.distribution(g)) - 1
maxComponent <- decompose.graph(g, min.vertices=(maxComponentSize -1), max.comps = 1)[[1]]
 ## Plot distribution of degrees of nodes.
pdf(paste(args[2], "averageDegree.pdf"))

# TODO calculate maximum degree and use this instead of xlim=c(2,60)

plot(degree.distribution(maxComponent)[2:60], xlab="degree", ylab="frequency", col='blue', type="l", xlim=c(2,60), lwd=3, bty="n")
lines(degree.distribution(g)[2:60], col='red', lwd=3)
xline(sum(degree(g, mode="out")) / length(V(g)), col='red')
xline(sum(degree(maxComponent, mode="out")) / length(V(maxComponent)), col='blue')

dev.off()



#################################################################
# Get bar plot for component size / component count
#################################################################

pdf(paste(args[2], "componentSizeComponentCount.pdf"))
barplot(table(clusters(g)$csize), xlab="Component Size", ylab="Frequency")

dev.off()

# Quit execution of r
q()