import sys
import csv
import numpy
import getopt
import math
import copy

def usage():
    print '$> python generatednadata.py <required args> [optional args]\n' + \
        '\t-c <#>\t\tNumber of clusters to generate\n' + \
        '\t-p <#>\t\tNumber of points per cluster\n' + \
        '\t-l <#>\t\tLength of DNA\n' + \
        '\t-o <file>\tFilename for the output of the raw data\n'

def distance(d1, d2):

    dis = 0
    for i in range(len(d1)):
        if (d1[i] != d2[i]):
            dis = dis + 1
    return dis

def tooClose(p1, plist, minDistance):
    '''
    Computes the distance between the dna and all dnas
    in the list, and if any dnas in the list are closer than minDistance,
    this method returns true.
    '''
    for p2 in plist:
        if (distance(p1, p2) < minDistance):
            return True
    return False

def handleArgs(args):
    # set up return values
    numClusters = -1
    numPoints = -1
    Length = -1
    output = None

    try:
        optlist, args = getopt.getopt(args[1:], 'c:p:l:o:')
    except getopt.GetoptError, err:
        print str(err)
        usage()
        sys.exit(2)

    for key, val in optlist:
        # first, the required arguments
        if   key == '-c':
            numClusters = int(val)
        elif key == '-p':
            numPoints = int(val)
        elif key == '-l':
            Length = int(val)
        # now, the optional argument
        elif key == '-o':
            output = val

    # check required arguments were inputted  
    if numClusters < 0 or numPoints < 0 or Length < 0 or output is None:
        usage()
        sys.exit()
    return (numClusters, numPoints, Length, output)

def drawOrigin(Length):
    return numpy.random.randint(4, size = Length)

# start by reading the command line
numClusters, numPoints, Length, output = handleArgs(sys.argv)

writer = csv.writer(open(output, "w"))

# step 1: generate each DNA centroid
centroids_radii = []
minDistance = 0
for i in range(0, numClusters):
    centroid_radius = drawOrigin(Length)
    # is it far enough from the others?
    while (tooClose(centroid_radius, centroids_radii, minDistance)):
        centroid_radius = drawOrigin(Length)
    centroids_radii.append(centroid_radius)

# step 2: generate the points for each centroid
d = ['A', 'T', 'G', 'C']
minLambda = 0.05;
maxLambda = 0.3;
for i in range(0, numClusters):
   	# compute the variance for this cluster
    lamb = numpy.random.uniform(minLambda, maxLambda)
    cluster = centroids_radii[i]
    for j in range(0, numPoints):
        # generate a DNA point with specified lambda
        # point is poisson-distributed around centroids[i]
        s = numpy.random.poisson(lamb, Length)
        dna = copy.deepcopy(cluster)
        for k in range(len(cluster)):
            if (s[k] > 0):
                dna[k] = (dna[k] + numpy.random.randint(3)) % 3
        # write the points out
        realdna = ""
        for l in range(len(dna)):
            realdna += d[dna[l]]
        print realdna
        writer.writerow([realdna])

