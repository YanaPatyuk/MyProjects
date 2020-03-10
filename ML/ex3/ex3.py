import numpy as np

def softmax(X):
    expX = np.exp(X)
    return expX / expX.sum()

def training(etha, train_x, train_y,ep_num,params,dev_x,dev_y):
    """
    This function train over the data
    :param etha:
    :param train_x:
    :param train_y:
    :param ep_num:
    :param params:
    :param dev_x:
    :param dev_y:
    :return:
    """
    lines_range = np.arange(train_x.shape[0])
    #run the forward_prop for epoch num
    for i in range(ep_num):
        sum = 0.0
        #shuffle
        x_train,y_train = shuffling_x_y(train_x, train_y, lines_range)
        #for eac example train
        for cur_x,cur_y in zip(x_train,y_train):
            forward_ret = forward_prop(cur_x,cur_y,params)
            back_ret = back_prop(forward_ret)
            sum+= forward_ret['loss'] # for loss calculation
            params = update_param(forward_ret, back_ret, etha)
        # loss_avg = sum / train_x.shape[0]
        # print("loss avg ",loss_avg)
        validate(params,dev_x,dev_y)

    return params


def validate(params, validation_x, validation_y):
    """
    Go over the validation set and check how accurate the model
    :param params:
    :param validation_x:
    :param validation_y:
    :return:
    """
    mistake_sum = 0.0
    for x, y in zip(validation_x, validation_y):
        out = forward_prop(x, y, params)
        # loss = out['loss']
        # loss_summing += loss
        if y != out['h2'].argmax():
            mistake_sum += 1
    print("succes averrage", 1 - (mistake_sum / validation_x.shape[0]))
    # print("averge loss",loss_summing / validation_x.shape[0] )

def testing(test_x, params):
    """
    Run over test_x and wirte the classification in test_y file
    :param test_x:
    :param params:
    :return:
    """
    file_test_y = open("test_y", "w")
    for x in test_x:
        forward_ret = forward(params, x)
        str_class = str(get_classification(forward_ret))
        file_test_y.write(str_class+'\n')
    file_test_y.close()

def forward(params,x):
    """
    forward x in layers without loss calculation
    :param params:
    :param x:
    :return:
    """
    W1, b1, W2, b2 = [params[key] for key in ('W1', 'b1', 'W2', 'b2')]
    x_reshped = x.reshape(784, 1)
    z1 = np.dot(W1, x_reshped) + b1       #w1*x + b1
    h1 = relu_activation(z1)
    z2 = np.dot(W2, h1) + b2
    return softmax(z2)

def update_param(old_params, grad_params, eta):
    """
    Update the  new wights
    :param old_params:
    :param grad_params:
    :param eta:
    :return:
    """
    W1_new, b1_new, W2_new, b2_new = [grad_params[key] for key in ('W1', 'b1', 'W2', 'b2')]
    W1, b1, W2, b2 = [old_params[key] for key in ('W1', 'b1', 'W2', 'b2')]
    W1 = W1 - eta * W1_new
    W2 = W2 - eta * W2_new
    b1 = b1 - eta * b1_new
    b2 = b2 - eta * b2_new
    return {'b1': b1, 'W1': W1, 'b2': b2, 'W2': W2}


def relu_activation(X):
    return np.maximum(X, 0)

def relu_deriative(X):
    return 1. * (X > 0)

def forward_prop(x, y, params):
    #get the data needed
    W1, b1, W2, b2 = [params[key] for key in ('W1', 'b1', 'W2', 'b2')]
    x_reshped = x.reshape(784, 1)
    z1 = np.dot(W1, x_reshped) + b1       #w1*x + b1
    h1 = relu_activation(z1)  # g(z1)
    z2 = np.dot(W2, h1) + b2  # w2*h1 + b2
    h2 = softmax(z2)  # g2=softmax(z2)
    loss = -np.log(h2[int(y)]) #sum - ylog(h2) -> -log(h2[y])
    ret = {'x': x, 'y': y, 'z1': z1, 'h1': h1, 'z2': z2, 'h2': h2, 'loss': loss}
    for key in params:
        ret[key] = params[key]
    return ret
#backpropogation
def back_prop(fprop_cache):
  x, y, z1, h1, z2, h2, loss = [fprop_cache[key] for key in ('x', 'y', 'z1', 'h1', 'z2', 'h2', 'loss')]
  vec_y = convart_classificaion_one_hot(y,10)
  dz2 = (h2 - vec_y)                                #  dL/dz2
  dW2 = np.dot(dz2, h1.T)                       #  dL/dz2 * dz2/dw2

  db2 = dz2                                     #  dL/dz2 * dz2/db2 =  dL/dz2
  dz1 = np.dot(fprop_cache['W2'].T, (h2 - vec_y)) * relu_deriative(z1) #  dL/dz2 * dz2/dh1 * dh1/dz1

  dW1 = np.dot(dz1, x.T.reshape(1, 784))                        #  dL/dz2 * dz2/dh1 * dh1/dz1 * dz1/dw1
  db1 = dz1                                     #  dL/dz2 * dz2/dh1 * dh1/dz1 * dz1/db1
  return {'b1': db1, 'W1': dW1, 'b2': db2, 'W2': dW2}


def get_classification(Y):
    return np.argmax(Y)
# create validation set and train
def spilt_train(x_train,y_train,t_size):
    validset_x, validset_y = x_train[t_size:, :], y_train[t_size:]
    x_train, y_train = x_train[:t_size, :], y_train[:t_size]
    return x_train, y_train,validset_x, validset_y
def convart_classificaion_one_hot(classifications,size):
    """
    Convart vector to one-hot mode
    ie 3 -> 0 0 1 0 0 0
    :param classifications:
    :param size:
    :return:
    """
    vec_y = np.reshape(np.zeros(size), (size, 1))
    vec_y[int(classifications)] = 1
    return vec_y
# shuffle train_x train_y together
def shuffling_x_y(x_tr,y_tr,range_of_lines):
    #shuffle indices
    np.random.shuffle(range_of_lines)
    shuf_y= y_tr[range_of_lines]
    shuf_x = x_tr[range_of_lines]
    return shuf_x,shuf_y
#main
if __name__ == "__main__":
    # Initialize random parameters and inputs
    input_size = 784
    h_rows_size = 50
    num_of_classes = 10
    # Initialize random parameters and inputs
    W1 = np.random.uniform(-0.08, 0.08, [h_rows_size, input_size])
    b1 = np.random.rand(h_rows_size, 1)
    W2 = np.random.uniform(-0.08, 0.08, [num_of_classes, h_rows_size])
    b2 = np.random.rand(num_of_classes, 1)
    params = {'W1': W1, 'b1': b1, 'W2': W2, 'b2': b2}
    # loading and normalizing
    x_train = np.loadtxt("train_x",max_rows= 5000)/255.0
    y_train = np.loadtxt("train_y",max_rows=5000)
    test_x = np.loadtxt("test_x")/255.0

    num_lines = x_train.shape[0]
    # shuffle train
    shuffling_x_y(x_train,y_train,np.arange(num_lines))
    t_size = (int)(num_lines*0.8)
    x_train, y_train,validset_x,validset_y = spilt_train(x_train,y_train,t_size)
    epoch = 5
    params = training(0.01, x_train, y_train, epoch, params, validset_x, validset_y)
    testing(test_x,params)