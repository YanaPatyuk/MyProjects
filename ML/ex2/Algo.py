import numpy as np
import random

labels = [[0,0,1],[0,1,0],[1,0,0]]
final_labal = [0,1,2]
seed = 480

def perceptron_train_mode(X_train, Y_train, feature_size = 10,eta = 0.1,epoch = 10):
    """
    Train perceptron
    :param X_train:
    :param Y_train:
    :param feature_size:
    :param eta:
    :param epoch:
    :return:
    """
    w = np.zeros((len(final_labal), feature_size))
    index = np.arange(len(Y_train))
    random.seed(480)
    for e in range(epoch):
        random.shuffle(index)
        for i in index:
            x = X_train[i]
            y = Y_train[i]
            # predict
            tamp = np.dot(w, x)
            y_hat = get_index(tamp)
            # update
            if y != y_hat:
                w[y, :] = w[y, :] + eta * x
                w[y_hat, :] = w[y_hat, :] - eta*x
        eta = eta**2

    return w


def test_mode(w, test_x):
    """
    Calculate the classification of test_x
    :param w:
    :param test_x:
    :return: classification
    """
    tamp = np.dot(w, test_x)
    y_hat = np.argmax(tamp)
    return y_hat


def get_index(array):
    """
    get indes
    :param array:
    :return:
    """
    return np.argmax(array)
    max = 0
    max_val = np.amax(array)
    for i in  array:
        if i == max_val:
            return max
        max += 1


def get_left_index(index1,index2):
    """
    return the left index of the array
    :param index1:
    :param index2:
    :return:
    """
    for i in final_labal:
        if i != index1 and i != index2:
            return i


def svm_train_mode(train_x, train_y, feature_size = 10, eta = 0.1,epoch = 10, lambada = 0.05):
    """
    SVM
    :param train_x:
    :param train_y:
    :param feature_size:
    :param eta:
    :param epoch:
    :param lambada:
    :return:
    """
    w = np.zeros((len(final_labal), feature_size))
    random.seed(500)
    index = np.arange(len(train_y))

    for e in range(epoch):
        random.shuffle(index)
        for i in index:
            x = train_x[i]
            y = train_y[i]
            # predict
            tamp = np.dot(w, x)
            y_hat = get_index(tamp)
            # update
            if y != y_hat:
                loss_waight = (1-lambada*eta)
                w[y, :] = loss_waight*w[y, :] + eta * x
                w[y_hat, :] = loss_waight*w[y_hat, :] - eta*x
                w[get_left_index(y,y_hat), :] = loss_waight*w[get_left_index(y,y_hat), :]
        eta = eta**2
    return w


def pa_train_mode(train_x,train_y,feature_size = 10,epoch = 10):
    """
    PA algorithem
    :param train_x:
    :param train_y:
    :param feature_size:
    :param epoch:
    :return:
    """
    w = np.zeros((len(final_labal), feature_size))
    random.seed(500)
    index = np.arange(len(train_y))
    for e in range(epoch):
        random.shuffle(index)
        for i in index:
            x = train_x[i]
            y = train_y[i]
            # predict
            tamp = np.dot(w, x)
            y_hat = get_index(tamp)
            # update
            if y != y_hat:
                norm = np.linalg.norm(x)
                if norm == 0: norm = 0.001
                T = (max(0, (1 -np.dot(x,w[y, :]) + np.dot(x,w[y_hat, :])))) / (2 * (norm ** 2))
                w[y, :] = w[y, :] + T * x
                w[y_hat, :] = w[y_hat, :] - T * x
    return w

