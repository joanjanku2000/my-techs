// a must for interviews
// js hash object as hashtables
// python has dictionaries

/**
 * Hash tables -> key value pair
 * basket.grapes = 100
 * key = 'grapes' -> done with hash function -> converts the key to a numbered value
 * value = 100
 */

/**
 * A hash function -> a function that generates a value of fixed length for each inputs that it gets
 */

class HashTable {
    constructor(size){
        this.data = new Array(size);
    }
    /**
     * Developer standard for having underscore -> meaning it should be private
     * more like a convention it doesn't really work
     * @param {} key 
     * @returns 
     */
    _hash(key){
        let hash  = 0;
        for (let i = 0;i<key.length;i++){
            hash = (hash + key.charCodeAt(i)*i) % this.data.length;
        }
        return hash;
    }
    setAsChaining(key,value){
        let index = this._hash(key);
        if (!this.data[index]){
            this.data[index] = [];
            this.data[index].push({key,value});
        } else {
            this.data[index].push({key,value})
        }
        
    }
    set(key,value){
        let assigned = false;
        let index = this._hash(key);
        if (!this.data[index]){
            this.data[index] = [key,value];
        } else {
            console.log("Collison: Iterating")
            for (let i = index ;i< this.data.length;i++){
                if (!this.data[i])
                   {
                    this.data[i] = [key,value];
                    assigned = true;    
                   } 
            }
            if (!assigned){
                for (let i = 0;i<index;i++) {
                    if (!this.data[i]) {
                     this.data[i] = [key,value];
                     assigned = true;    
                    } 
                }
                if (!assigned){
                    console.log("No space left");
                    return false;
                }
            }
        }
        return true;
    }
    getFromChaining(key){
        let index = this._hash(key);
        let chained = this.data[index];
        if (chained){
            for (let i = 0;i<chained.length;i++){
                if (chained[i].key === key){
                    return chained[i];
                }
            }
        }
        
        console.log("Not found");
        return false;
    }
    get (key){
        let index = this._hash(key);
        
        if (typeof this.data[index] != 'undefined' && this.data[index][0]===key){
            return this.data[index][1];
        }
        else{
            
            for (let i = index+1;i<this.data.length;i++){
                if ( typeof this.data[index] != 'undefined' && this.data[i][0]===key){
                    return this.data[i][1];
                }
            }
            for (let i = 0;i<index;i++){
                if (typeof this.data[index] != 'undefined' && this.data[i][0]===key){
                    return this.data[i][1];
                }
            }
        }
        console.log("Not found");
        return false;
    }
    keys(){
        const keysArray = [];
        for (let i = 0;i<this.data.length;i++){
            if (this.data[i]){
                for (let j=0 ; j<this.data[i].length;j++){
                    keysArray.push(this.data[i][j].key);
                }
              
            }
        }
        return keysArray
    }
}

const myHashTable = new HashTable(5);
myHashTable.setAsChaining('grapes',10);
myHashTable.setAsChaining('apples',100);
myHashTable.setAsChaining('bananas',1000);
myHashTable.setAsChaining('cherries',10000);
myHashTable.setAsChaining('luleshtrydhe',100000);
console.log(myHashTable.data)
console.log("Findng data with key 'luleshtrydhe' ");
console.log(myHashTable.getFromChaining("luleshtrydhe"));
console.log(myHashTable.keys())
//myHashTable.get('grapes');