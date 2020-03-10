import Algo
import sys
import numpy as np

final_classification = [0,1,2]

Sex = {'M': [0,0,1], 'F':[0,1,0], 'I': [1,0,0] }

Classfications = {0: [0,0,1], 1:[0,1,0], 2: [1,0,0]}


feature_table = ["Sex", "Length","Diameter","Height","Whole weight","Shucked weight","Viscera weight","Shell weight"]
feature_size = len(feature_table) + 2
feature_table_min = None
feature_table_max = None
feature_mean = None
feature_std = None


def __main__():
    #read args
    if len(sys.argv)==1:
        train_x_file = "train_x_2.txt"
        train_y_file = "train_y_2.txt"
        test_x = "test_x_2.txt"
        test_y = "test_y_2.txt"
    else:
        train_x_file = sys.argv[1]
        train_y_file = sys.argv[2]
        test_x = sys.argv[3]

    #  Read data from files
    train_y = get_classification_from_file(train_y_file)
    train_x = get_data_from_file(train_x_file)
    test = get_data_from_file(test_x)

    test_y = get_classification_from_file(test_y) #used for testing

    # copy lists so we can normilize in two ways
    train_x_c = train_x.copy()
    test_c = test.copy()

    #  Get info from data for normalization
    global feature_table_max, feature_table_min, feature_mean, feature_std
    feature_table_max = np.amax(train_x,axis=0)#-another option for min-max
    feature_table_min = np.amin(train_x,axis=0)
    #feature_table_max = [1]*feature_size
    #feature_table_min = [0]*feature_size
    feature_mean, feature_std = calculate_mean_and_stand_dev(train_x)


    #  normilize the data
    # min-mac
    train_x_min_max = normalization_min_max_colume(train_x)
    test_min_max = normalization_min_max_colume(test)
    #zscore
    train_x_zscore = normalization_zscore(train_x_c,feature_mean,feature_std)
    test_zscore = normalization_zscore(test_c,feature_mean,feature_std)
    #run the algorithem
   # train_test_and_print(train_x_min_max, train_y, test_min_max,train_x_zscore,test_zscore)

    testing_perceptron_debug(train_x_min_max, train_y, test_min_max, test_y, train_x_zscore, train_y, test_zscore, test_y)

def get_data_from_file(file_name):
    """
    Read Data from fileName and convart to narray
    :param file_name:
    :return: narray
    """
    with open(file_name, mode='rb') as file:
        data_set_raw = [x.decode('utf8').strip() for x in file.readlines()]
    file.close()
    data_set = np.zeros(feature_size)
    for set in data_set_raw:
        set = set.split(",") # split to tokens
        sex = Sex[set[0]]  # get the sex of set to convart to nomerical
        set =np.array(np.append(sex,set[1:]),float)  # convart set to numbers from string
        data_set = np.vstack((data_set,set))
    data_set = np.delete(data_set, 0, 0)
    return data_set


def get_classification_from_file(file_name):
    """
    Read classificaion data from file and convart to narray
    :param file_name:
    :return:
    """
    with open(file_name, mode='rb') as file:
        classification_train = [x.decode('utf8').strip() for x in file.readlines()]
    file.close()
    return np.array(classification_train, float).astype(int)


def normalization_min_max_colume(data):
    """
    Normilze data by min-max algo
    :param data:
    :return: normilized data
    """
    for d in data:
        for i in range(feature_size):
            if feature_table_min[i] != feature_table_max[i]:
                d[i] = (d[i] - feature_table_min[i]) / (feature_table_max[i] - feature_table_min[i])
    return data

def calculate_mean_and_stand_dev(data):
    """
    Calculate mean and stand_dev on given data
    :param data: learning
    :return: mean and stand dev
    """
    #return stats.zscore(b, axis=0, ddof=1) """this is the easy way """
    feature_mean = np.mean(data, axis=0)

    """this part calculates manualy the stand_dev-i used the function insted
    feauter_stand_dev = list([0]*feature_size)
    for i in data:
        for j in range(feature_size):
            m = feauter_stand_dev[j]
            feauter_stand_dev[j] = m + np.power((i[j]-feature_mean[j]), 2)
    feauter_stand_dev = np.array(feauter_stand_dev)/len(b)
    feauter_stand_dev = np.sqrt(feauter_stand_dev)
    """
    feauter_stand_dev = np.std(data, axis=0)
    feature_std = feauter_stand_dev
    return feature_mean, feature_std

def normalization_zscore(data, mean, std):
    """
    Calculate z-score by given mean and stand_dev
    :param data:
    :param mean:
    :param stand_dev:
    :return: normilaized data
    """
    for vector in data:
        for j in range(feature_size):
            tamp = vector[j]-mean[j]
            vector[j] = tamp/std[j]
    return data


def train_test_and_print(train_x,train_y,test_x, train_x_zscore, test_x_zscore):
    """
        This function trains the percrptrons and print the lables agter testing

    :param train_x:
    :param train_y:
    :param test_x:
    :param train_x_zscore:
    :param test_x_zscore:
    :return:
    """
    w_perceptron = Algo.perceptron_train_mode(train_x,train_y,feature_size,0.01,11)
    w_svm = Algo.svm_train_mode(train_x_zscore,train_y,feature_size,0.1,10,0.7)
    w_pa = Algo.pa_train_mode(train_x_zscore,train_y,feature_size,10)
    for set_minmax, set_zscore in zip(test_x,test_x_zscore):
        label_perc = Algo.test_mode(w_perceptron,set_minmax)
        label_svm = Algo.test_mode(w_svm,set_zscore)
        label_pa = Algo.test_mode(w_pa, set_zscore)
        ans = list()
        ans.append("perceptron: ")
        ans.append(str(label_perc))
        ans.append(", svm: ")
        ans.append(str(label_svm))
        ans.append(", pa: ")
        ans.append(str(label_pa))
        print(''.join(ans))




def testing_perceptron_debug(train_x, train_y,test_x,test_y,trainZ,trainyz,TestZ,TestYZ):
    w_perceptron = Algo.perceptron_train_mode(train_x,train_y,feature_size,0.1,11)
    w_svm = Algo.svm_train_mode(trainZ,train_y,feature_size,0.1,10,0.7)
    w_pa = Algo.pa_train_mode(trainZ,train_y,feature_size,10)
    counter_per = 0
    counter_svm = 0
    counter_pa = 0
    print("yana")
    for i,j in zip(test_x, test_y):
        if Algo.test_mode(w_perceptron, i) == j:
            counter_per += 1
    for i,j in zip(TestZ,TestYZ):
        if Algo.test_mode(w_svm, i) == j:
            counter_svm+=1
        if Algo.test_mode(w_pa, i) == j:
            counter_pa +=1
    size_sub = len(test_y)
    print("perceptron is ", counter_per/size_sub, "svm is: ", counter_svm/size_sub, "pa is: ", counter_pa/size_sub)

    counter_per = 0
    counter_svm = 0
    counter_pa = 0
    for i,j in zip(trainZ,train_y):
        if Algo.test_mode(w_svm, i) == j:
            counter_svm+=1
        if Algo.test_mode(w_pa, i) == j:
            counter_pa +=1
    for i,j in zip(train_x, train_y):
        if Algo.test_mode(w_perceptron, i) == j:
            counter_per += 1
    size_sub = len(train_y)
    print("perceptron is ", counter_per/size_sub, "svm is: ", counter_svm/size_sub, "pa is: ", counter_pa/size_sub)


__main__()






"""
def check(train_x, train_y,test_x,test_y,normalization_mode,file):
    etas = [0.1, 0.01]
    lambda_c = [0.7, 0.07, 0.01, 0.1]
    epoch = [9, 10, 11, 12, 13,14,15]
    file.write("Test mode normal: ")
    file.write(normalization_mode)
    file.write("\n")
    print("For normalization: ", normalization_mode)
    for epo in epoch:
        w_pa = Algo.pa_train_mode(train_x, train_y, 10, epo)
        for eta in etas:
            w_perceptron = Algo.perceptron_train_mode(train_x, train_y, 10, eta, epo)
            for lamb in lambda_c:
                file.write("current paramtes:\n")
                l = []
                l.append(" epoch: ")
                l.append(str(epo))
                l.append(" eta: ")
                l.append(str(eta))
                l.append(" lambda: ")
                l.append(str(lamb))
                l.append("\n")
                file.write(''.join(l))
                print("current paramtes:")
                print("epoch:", epo, "eta", eta, "lambda:", lamb)
                w_svm = Algo.svm_train_mode(train_x, train_y, 10, eta, epo, lamb)
                counter_per = 0
                counter_svm = 0
                counter_pa = 0
                for i, j in zip(test_x, test_y):
                    if Algo.test_mode(w_perceptron, i) != j:
                        counter_per += 1
                    if Algo.test_mode(w_svm, i) != j:
                        counter_svm += 1
                    if Algo.test_mode(w_pa, i) != j:
                        counter_pa += 1
                size_sub = len(test_y)
                l = []
                l.append(" error is: perceptron is :")
                l.append( str(counter_per / size_sub))
                l.append(" svm is :")
                l.append(  str(counter_svm / size_sub))
                l.append(" pa is: ")
                l.append(str(counter_pa / size_sub))
                l.append("\n")
                file.write(''.join(l))
                print("error is: perceptron is ", counter_per / size_sub, "svm is: ", counter_svm / size_sub, "pa is: ",
                      counter_pa / size_sub)
                print()

"""
