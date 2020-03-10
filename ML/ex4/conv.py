
from sklearn.metrics import confusion_matrix
import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import numpy as np
from torch.utils.data import Dataset
from torch.utils.data.sampler import SubsetRandomSampler
from torchvision import datasets, transforms, models
from gcommand_loader import GCommandLoader


class ConvNet(nn.Module):
    def __init__(self):
        super(ConvNet, self).__init__()
        self.conv_first_layer = nn.Conv2d(1, 17, 5)
        self.pool = nn.MaxPool2d(2, 2)
        self.sec_conv_layer = nn.Conv2d(17, 35, 5)
        self.conv_layer3 = nn.Conv2d(35,22,5)
        self.first_linear_layer = nn.Linear(22*16*9, 100)
        self.sec_linear_layer = nn.Linear(100, 85)
        self.linear_layer3 = nn.Linear(85, 30)
        self.drop_out = nn.Dropout()

    def forward(self, x):
        # print(x.shape)
        x = self.conv_first_layer(x)
        x = F.relu(x)
        x = self.pool(x)
        # print(x.shape)
        x = self.sec_conv_layer(x)
        x = F.relu(x)
        x = self.pool(x)
        x = self.conv_layer3(x)
        x = F.relu(x)
        x = self.pool(x)        # print(x.shape)
        x = x.view(-1, 22*16*9)
        x = self.drop_out(x)
        x = F.relu(self.first_linear_layer(x))
        x = F.relu(self.sec_linear_layer(x))
        x = self.linear_layer3(x)
        #return F.log_softmax(x, dim=1)
        return x