import numpy as np
import scipy.io as sio
import matplotlib.pyplot as plt
from scipy.misc import imread


class Pixel:
    """
    Class for pixel
    """
    def __init__(self, point):
        self.point = point
        self.classification = None
        self.distance = None


def loadPic():
    """
    Load pic to array
    :return: copy of original X, new lisf of pixels, image size
    """
    # data preperation (loading, normalizing, reshaping)
    path = 'dog.jpeg'
    A = imread(path)
    A = A.astype(float) / 255.
    img_size = A.shape
    X = A.reshape(img_size[0] * img_size[1], img_size[2])
    listOfPixel= []
    for pixel in X:
        listOfPixel.append(Pixel(pixel))

    return X.copy(), listOfPixel,img_size


def init_centroids(X, K):
    """
    Initializes K centroids that are to be used in K-Means on the dataset X.

    Parameters
    ----------
    X : ndarray, shape (n_samples, n_features)
        Samples, where n_samples is the number of samples and n_features is the number of features.
    K : int
        The number of centroids.

    Returns
    -------
    centroids : ndarray, shape (K, n_features)
    """
    if K == 2:
        return np.asarray([[0.        , 0.        , 0.        ],
                            [0.07843137, 0.06666667, 0.09411765]])
    elif K == 4:
        return np.asarray([[0.72156863, 0.64313725, 0.54901961],
                            [0.49019608, 0.41960784, 0.33333333],
                            [0.02745098, 0.        , 0.        ],
                            [0.17254902, 0.16862745, 0.18823529]])
    elif K == 8:
        return np.asarray([[0.01568627, 0.01176471, 0.03529412],
                            [0.14509804, 0.12156863, 0.12941176],
                            [0.4745098 , 0.40784314, 0.32941176],
                            [0.00784314, 0.00392157, 0.02745098],
                            [0.50588235, 0.43529412, 0.34117647],
                            [0.09411765, 0.09019608, 0.11372549],
                            [0.54509804, 0.45882353, 0.36470588],
                            [0.44705882, 0.37647059, 0.29019608]])
    elif K == 16:
        return np.asarray([[0.61568627, 0.56078431, 0.45882353],
                            [0.4745098 , 0.38039216, 0.33333333],
                            [0.65882353, 0.57647059, 0.49411765],
                            [0.08235294, 0.07843137, 0.10196078],
                            [0.06666667, 0.03529412, 0.02352941],
                            [0.08235294, 0.07843137, 0.09803922],
                            [0.0745098 , 0.07058824, 0.09411765],
                            [0.01960784, 0.01960784, 0.02745098],
                            [0.00784314, 0.00784314, 0.01568627],
                            [0.8627451 , 0.78039216, 0.69803922],
                            [0.60784314, 0.52156863, 0.42745098],
                            [0.01960784, 0.01176471, 0.02352941],
                            [0.78431373, 0.69803922, 0.60392157],
                            [0.30196078, 0.21568627, 0.1254902 ],
                            [0.30588235, 0.2627451 , 0.24705882],
                            [0.65490196, 0.61176471, 0.50196078]])
    else:
        print('This value of K is not supported.')
        return None


def getDistance(point, centroid):
    """
    return the distance between pixel and centroid
    """
    #return np.linalg.norm(point.point-centroid)
    return ((point.point[0]-centroid[0])**2)+((point.point[1]-centroid[1])**2)+((point.point[2]-centroid[2])**2)


def setCentroid(kCentroids, point):
    """
    :param kCentroids: list of centroids
    :param point: to check
    """
    # set the first distance
    point.distance = getDistance(point,kCentroids[0])
    point.classification= kCentroids[0]
    for k in kCentroids:
        distance = getDistance(point, k)
        # if the current distance is smaller-update
        if distance < point.distance:
            point.classification = k
            point.distance = distance


def updateCentroids(centroids, pixelList):
    """
    Update the centroids to new values by the avg of classification
    The index refers to each centroid
    :param centroids: to update
    :param pixelList: classified
    """
    k = len(centroids)
    centoidsCount = [0]*k #couts how many pixels classified for each cent.
    centroidsSum = np.zeros([k, 3])#sum value of centroids
    for pixel in pixelList:
        index = 0
        #find whitch centroid equals
        for centroid in centroids:
            if np.array_equal(pixel.classification, centroid):
                centoidsCount[index] += 1
                centroidsSum[index] += pixel.point
                break
            index += 1
    index = 0
    for centroid in centroidsSum:
        centroids[index] = centroid/centoidsCount[index]
        index += 1


def setCentroids(centroids,pixelList):
    """
    updates the centroids and calculate the loss
    :param centroids:
    :param pixelList:
    :return: loss value
    """
    loss_graph_value = 0
    for pixel in pixelList:
        setCentroid(centroids, pixel)
        loss_graph_value += pixel.distance#sun of the normal disdance^2 to calculate loss
    loss_graph_value = loss_graph_value / len(pixelList)
    return loss_graph_value


def print_cent(cent):
    """
    Print func as given.
    :param cent:
    :return: string of centroids to print
    """
    if type(cent) == list:
        cent = np.asarray(cent)
    if len(cent.shape) == 1:
        return ' '.join(str(np.floor(100*cent)/100).split()).replace('[ ', '[').replace('\n', ' ').replace(' ]',']').replace(' ', ', ')
    else:
        return ' '.join(str(np.floor(100*cent)/100).split()).replace('[ ', '[').replace('\n', ' ').replace(' ]',']').replace(' ', ', ')[1:-1]

def kmeansAlgo(centroids, pixelList):
    lossList = []
    print("iter 0:", print_cent(centroids))
    for i in range(10):
        lossList.append(setCentroids(centroids,pixelList))
        updateCentroids(centroids, pixelList)
        print("iter " + str(i+1) +":", print_cent(centroids))
    #we add the lats loss for the final graph.
    lossList.append(setCentroids(centroids,pixelList))
    return lossList


def changePic(newPixelList, oldPixel, image_size):
    """
    This func used only for convart old pic-pixels to new values and show
    :param newPixelList: list of pixels whit new values-type *Pixel*
    :param oldPixel: list of originals pixel values
    :return:
    """
    index = 0
    for pixel in newPixelList:
        oldPixel[index] = pixel.classification
        index+=1
    l = oldPixel.reshape(image_size)
    plt.imshow(l)
    plt.grid(False)
    plt.show()


def printGraph(k, lossList):
    """
    Print plot:
    The average loss value (i.e. the distance between each point to its closest
    centroid) as a function of the iterations for k = 2, 4, 8, 16
    :param k: iteration
    :param lossList: loss values
    """
    plt.plot(lossList, 'bo',lossList, 'k')
    plt.title(' Number centroids k = %d' % k)
    plt.ylabel('loss values')
    plt.xlabel('iteration number')
    plt.xlim(0, 10)
    y = range(11)
    for a,b in zip(y, lossList):
        plt.text(a, b, str(np.floor(b*10000)/10000))
    plt.show()


def __main__():
    pic_norm,pic_pixel,image_size= loadPic()
    rangeOfK =[2, 4, 8, 16]
    for k in rangeOfK:
        centroids = init_centroids(pic_pixel, k)
        print("k="+str(k) +":")
        loss = kmeansAlgo(centroids, pic_pixel)
        """"
        #print loss graph
        printGraph(k,loss)
        #This part used to show the pictures after classification      
        changePic(pic_pixel,pic_norm,image_size)
        """


__main__()
