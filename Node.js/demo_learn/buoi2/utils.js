function generateRandomNumber(){
    return Math.floor(Math.random() * 100);
}

function celcius2Fah(celcius){
    return (celcius * 9/5) + 32;
}

module.exports ={
    generateRandomNumber,
    celcius2Fah
};