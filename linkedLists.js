/**
 * Why linked lists?
 * JS doesn't have linked lists
 * 10 --> 5 --> 16
*/

// let myLinkedList = {
//     head:{
//         value:10,
//         next: {
//             value:5,
//             next : {
//                 value:16,
//                 next:null
//             }
//         }
//     }
// }
class Node {
    constructor(value){
        this.value = value;
        this.next = null;
    }
}
class LinkedList {
    constructor(value){
        this.head = new Node(value);
        this.tail = this.head
        this.length = 1;
    }

    append(value){
        let iterator = this.head;
        while (iterator.next != null){
            iterator = iterator.next;
        }
        iterator.next = new Node(value);
        this.length++;
        this.tail = iterator.next;
    }
    appendHead(value){
        console.log("Appending "+value + " as head");
        let node = new Node(value);
        node.next = this.head;
        this.head = node;
        this.length++;
    }
    appendInPositon(index,value){

        console.log("Appending "+value + " in positon "+index);
     
        if (index==0){
            this.appendHead(value);
            return;
        }
        if (index>this.length-1){
            this.append(value);
            return;
        }
        if (index<0){
            
            let pos = (this.length-index)%this.length;
        
            console.log("Appending negative pos "+pos)
            this.appendInPositon(pos,value) 
            return;
        }
        let node = new Node(value);
        let iterator = this.head.next;
        let prev = this.head;

        let i = 0;
        while(iterator != null) {
         
            if (i==index-1) {
                break;
            }
       
            prev = iterator;
            iterator=iterator.next;
            i++;
        } 
     
        prev.next = node;
        node.next = iterator;
        this.length++;
    }
    print(){
        let iterator = this.head;
        while (iterator.next != null){
            console.log(iterator.value + " --> ");
            iterator = iterator.next;
        }
        console.log(" "+ iterator.value + "-->null");
    }

    delete(index){
        console.log("Deleting in position "+index);
        let iterator = this.head.next;
        let prev = this.head;

        let i = 0;
        while(iterator != null) {
         
            if (i==index-1) {
                break;
            }
       
            prev = iterator;
            iterator=iterator.next;
            i++;
        } 
        prev.next = iterator.next;
        iterator.next = null;
        
        this.length--;
    }

}

const myLinkedList = new LinkedList(10)
myLinkedList.append(23)
myLinkedList.append(12);
myLinkedList.appendHead(13);
myLinkedList.appendInPositon(2,5);
myLinkedList.appendInPositon(0,1)
myLinkedList.appendInPositon(65,65)
myLinkedList.appendInPositon(-3,102)
myLinkedList.print();
myLinkedList.delete(3)
console.log("------printing linked list-------")
myLinkedList.print();
console.log(" Head is -> "+myLinkedList.head.value)
console.log(" Tail is -> "+myLinkedList.tail.value)
console.log("Total length of linked list is "+myLinkedList.length)
console.log(myLinkedList);