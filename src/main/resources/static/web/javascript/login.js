const {createApp} = Vue

const login = createApp({
    data(){
        return {
            accountClient:[],
            clientsAccount: {},
            
            
            
           


            
            
            
        }
    },
    created(){
        this.loadData()
        this.checkedAccount = localStorage.getItem("mode") 
    },
    methods: {
        loadData(){
            axios
            .get("http://localhost:8080/api/clients/1")
            .then((data) =>{
                this.accountClient = data.data.accountDTO
                this.username = data.data.firstName
                this.clientsAccount = this.accountClient.sort((a,b) => a.id - b.id)
                this.balanceTotal = this.clientsAccount.map(account => account.balance).reduce((iter, acc) => iter + acc)
                console.log(this.clientsAccount)

                

                
                
            })
            .catch((error) => console.log(error))
        },
        
        
       
        
        
            
    },
    computed: {
      

    
    
    
    }
})


login.mount('#login')