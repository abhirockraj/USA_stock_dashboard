import streamlit as st
import pandas as pd
import yfinance as yf
import matplotlib.pyplot as plt
import numpy as np
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
# from tensorflow.keras.models import Sequential
# from tensorflow.keras.layers import Dense
# from tensorflow.keras.layers import LSTM
from sklearn.preprocessing import MinMaxScaler

st.set_option('deprecation.showPyplotGlobalUse', False)
ticket_file= pd.read_csv('USA_stockTicker.csv',index_col=False)
st.title("USA Stock Analysis and Prediction")
ticket_file.columns =['Ticker', 'Company_Name']
cn = st.selectbox("Select Ticker",ticket_file['Company_Name'])
ticker = ticket_file[ticket_file['Company_Name'] == cn]['Ticker']
t= ticker.to_string(index=False)
st.write('TICKER = ',t)
df = yf.Ticker(t)
col1, col2 ,col3= st.columns(3)
number = st.number_input("Number of days/months/years",min_value=1,max_value=365,step=1 )
period1 = st.selectbox("Select days/months/years",('d','mo','y'),index=0)
col = st.selectbox("Select Parameter",('Open','High','Low','Close'),index=0)

def create_dataset(dataset, time_step=1):
	dataX, dataY = [], []
	for i in range(len(dataset)-time_step-1):
		a = dataset[i:(i+time_step), 0]   ###i=0, 0,1,2,3-----99   100 
		dataX.append(a)
		dataY.append(dataset[i + time_step, 0])
	return np.array(dataX), np.array(dataY)


if col1.button("Show Finacials "):
    st.dataframe (df.financials,width= 50000,height=50000)
if(col3.button("visulalise")):
    hist = df.history(period=str(number)+period1)
    plt.figure(figsize=(10,10))
    plt.plot(hist[col], color = 'g', label=col)
    st.pyplot()
if(col2.button("Predict and compare")):
    if(period1 != 'y'):
        st.write("Please Choose Time Frame at least 1 year also be sensible !!!")
    else:
        model=keras.Sequential()
        model.add(layers.LSTM(50,return_sequences=True,input_shape=(100,1)))
        model.add(layers.LSTM(50,return_sequences=True))
        model.add(layers.LSTM(50))
        model.add(layers.Dense(1))
        model.compile(loss='mean_squared_error',optimizer='adam')
        model.load_weights('./checkpoints/my_checkpoint')
        scaler=MinMaxScaler(feature_range=(0,1))
        hist = df.history(period=str(number)+period1)
        df_x_a=scaler.fit_transform(np.array(hist[col]).reshape(-1,1))
        time_step = 100
        x,y = create_dataset(df_x_a, time_step)
        x =x.reshape(x.shape[0],x.shape[1] , 1)
        y_pred=model.predict(x)
        look_back=100
        # shift test predictions for plotting
        testPredictPlot = np.empty_like(df_x_a)
        testPredictPlot[:, :] = np.nan
        testPredictPlot[look_back:len(y_pred)+look_back, :] = y_pred
        # plot baseline and predictions
        plt.plot(scaler.inverse_transform(df_x_a),color='b', label='Actual data')
        plt.plot(scaler.inverse_transform(testPredictPlot),color='r', label='Predicted data')
        plt.legend()
        st.pyplot()
