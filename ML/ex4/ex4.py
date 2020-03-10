from torch.utils.data import Dataset
from torch.utils.data.sampler import SubsetRandomSampler
from torchvision import datasets, transforms, models
from sklearn.metrics import confusion_matrix
import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import numpy as np

from conv import ConvNet
from gcommand_loader import GCommandLoader

def test(model,test_loader,my_device):

  f = open("test_y", "w") #create file
  
  model.eval()
 
  for i,( data, labels) in enumerate(test_loader):
    data, labels = data.to(my_device), labels.to(my_device)
    output = model(data)
    classification = output.data.max(1,keepdim=True)[1] # get the index of the max log-probability
	dataFileName=str(test_loader.dataset.spects[i][0].split("/")[5])
    f.write(dataFileName + ", " + str(classification[0].item())+"\n")

    
  


def valid(model,valid_loader,my_device):
    model.eval()
    test_loss = 0
    correct = 0
    for data, target in valid_loader:
        data, target = data.to(my_device), target.to(my_device)
        output = model(data)
        test_loss += F.nll_loss(output, target, size_average=False).item()  # sum up batch loss
        pred = output.data.max(1, keepdim=True)[1]  # get the index of the max log-probability
        correct += pred.eq(target.data.view_as(pred)).cpu().sum()
    test_loss /= len(valid_loader.dataset)
    print('\nTest set: Average loss: {:.4f}, Accuracy: {}/{} ({:.0f}%)\n'.format(test_loss, correct,len(valid_loader.dataset),100. * correct /
                                                                                 
                                                                                 len(valid_loader.dataset)))
def train(device, optimizer, num_epochs, model, train_loader, criterion):
    model.train()
    total_step = len(train_loader)
    loss_list = []
    acc_list = []
    
    for epoch in range(num_epochs):
        j = 0
        for i, (dat, labels) in enumerate(train_loader):
            j+=1
            dat,labels = dat.to(device),labels.to(device)
            outputs = model(dat)
            loss = criterion(outputs, labels)
            loss_list.append(loss.item())
            optimizer.zero_grad()
            loss.backward()
            optimizer.step()

def main():
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    validset = GCommandLoader('./ML4_dataset/data/valid')
    trainset = GCommandLoader('./ML4_dataset/data/train')
    testset = GCommandLoader('./ML4_dataset/data/test')
    valid_loader = torch.utils.data.DataLoader( validset, batch_size=100, shuffle=False, num_workers=20, pin_memory=True, sampler=None)
	#test batch size is 1 so we could ge each sound classification
    test_loader = torch.utils.data.DataLoader(
        testset, batch_size=1, shuffle=False,
        num_workers=20, pin_memory=True, sampler=None)
    train_loader = torch.utils.data.DataLoader(
        trainset, batch_size=100, shuffle=True,
        num_workers=20, pin_memory=True, sampler=None)
    
    model = ConvNet().to(device)
    # Loss and optimizer
    criterion = nn.CrossEntropyLoss()
    optimizer = torch.optim.RMSprop(model.parameters(), lr=0.001)
    #optimizer = torch.optim.Adam(model.parameters(), lr=0.001)
    train(device, optimizer, 5, model, train_loader, criterion)
    valid(model,valid_loader,device)
    test(model,test_loader,device)
    

if __name__ == '__main__':
    main()
