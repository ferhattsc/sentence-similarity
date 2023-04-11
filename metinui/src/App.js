import React, { useState,useEffect } from "react";
import axios from "axios";
import "./app.scss";


function App() {
  const [text, setText] = useState("");

  const [data, setData] = useState([]);
  const [currentId,setCurrentId]=useState("6420708869ab06365c4d5726")
  const [currentData,setCurrentData]=useState([])
  const [meaningSentence,setMeaningSentence]=useState("")
  const [responseTime,setResponseTime]=useState("")
  const [allData,setAllData]=useState([])

  const handleAddItemToData = () => {
    if (!text) {
      alert("lütfen text alanına doldurunuz");
    }
    let newDataSet = [...data];
    newDataSet.push(text);
    setData(newDataSet);
    setText("");
  };

  const handleSaveData = async () => {
    const differance=data.filter(x=>!currentData.includes(x))
    const obj = {
      id: currentId,
      eklenenMetinler: differance,
    };


    const res = await axios.post("http://localhost:8080/words/ekle" , obj)
    if(res.data){
        setCurrentId(res?.data?.id)
        setCurrentData(res?.data?.eklenenMetinler)
    }

  };

  const handleJoinData = async () => {
    const differance=data.filter(x=>!currentData.includes(x))
    const obj = {
      id: currentId,
      eklenenMetinler: differance,
    };

    const res = await axios.post("http://localhost:8080/words/birlestir" , obj)
    if(res.data){
           setCurrentId(res.data.id)
           setCurrentData(res.data.eklenenMetinler)
           setMeaningSentence(res.data.birlestirilmisAnlamliCumle)
            setResponseTime(res.data.responseTime)
       }
  };

  useEffect(()=>{
      const getAllData=async()=>{
          const res = await axios.get("http://localhost:8080/words/all")
          const allDataResponse=[]
          if(res.data){
            res.data.forEach(x=>{
                allDataResponse.push(x.birlestirilmisAnlamliCumle)
            })
          }
          setAllData(allDataResponse)
          return res
      }
      getAllData()
  },[])

  return (
    <div className="app-container">
      <div className="left-side">
        <div className="input-container">
          <h2>Girilecek Textler</h2>
          <div className="text-input-container">
            <span>Text Giriniz</span>
            <input
              value={text}
              onChange={(e) => setText(e.target.value)}
              type="text"
            />
            <div className="input-and-button">
              <button onClick={handleAddItemToData}> Ekle </button>
              <button onClick={handleJoinData}> Birleştir </button>
              <button onClick={handleSaveData}> Kaydet </button>
            </div>
          </div>

          <div className="all-text">
            <span className="all-title">Girilen Textler</span>
            {data.map((item) => (
              <span key={item} className="all-item">
                {item}
              </span>
            ))}
          </div>
        </div>
      </div>
      <div className="right-side">
        <div className="output-container">
          <span className="output-title">Birleştirilmiş Anlamlı Cümle</span>
          <span className="output-text">{meaningSentence}</span>
          {
          responseTime &&
          <span className="output-text">Response süresi: {responseTime} ms</span>
          }
        </div>
        <div className="output-container">
          <span className="output-title">Veri Tabanı Kayıtları </span>
          {
            allData.map((item,index)=>(
                <span key={index} className="output-text">{item}</span>

            ))
          }

        </div>
      </div>
    </div>
  );
}

export default App;
