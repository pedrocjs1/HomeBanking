const {createApp} = Vue

const account = createApp({
    data(){
        return {
            accountInfo:[],
            account: [],
            accountTransaction: [],
            accountName: "",
            
            username: "",
            accountBalance: "",
            

            checkedAccount: undefined,
            currentText: "",
            currentBackGround: "",
            mode: "",
            boxShadow: "",
            backgroundBody: "",
            backgroundLogo: "",

            checkedHeader: true,
            header: "",
            headerDesktop: "",
            id: new URLSearchParams(location.search).get("id"),
            accountId:[]

            
            
            
        }
    },
    created(){
        this.loadData()
        this.checkedAccount = localStorage.getItem("mode") 
    },
    methods: {
        loadData(){
            axios
            .get("http://localhost:8080/api/accounts")
            .then((data) =>{
                this.accountInfo = data.data

                this.account = [... data.data].find(element => element.id == this.id)
                this.accountTransaction = this.account.transactions.sort((a,b) => b.id - a.id)
                this.accountName = this.account.number
                this.accountBalance = this.account.balance
                
                console.log(this.account);
                   
            })
            .catch((error) => console.log(error))

            axios
            .get("http://localhost:8080/api/clients/1")
            .then((data) =>{
                // this.accountClient = data.data.accountDTO
                this.username = data.data.firstName
                

                

                
                
            })
            .catch((error) => console.log(error))
        },
        collapseHeaderMovile(){
            if (this.header == "mob-menu-opened"){
                this.header = ""
            } else {
                this.header = "mob-menu-opened"
            }
        },
        formatedDate(dateInput) {
            const date = new Date(dateInput)
            return date.toDateString().slice(3)
        },
        formatedHour(dateInput) {
            const date = new Date(dateInput)
            let minutes = date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()
            return date.getHours() + ":" + minutes
        },
        
       
        
        
            
    },
    computed: {
      darkMode(){
        if (this.checkedAccount === "true" || this.checkedAccount === true) {
          this.currentText = "text-white"
          this.currentBackGround = "bg-dark"
          this.mode = "Dark Mode"
          this.backgroundBody = "backgroundBody"
          this.backgroundLogo = ""
          localStorage.setItem("mode", true)

        } else if (this.checkedAccount === "false" || this.checkedAccount === false) {
          this.currentText = "text-black"
          this.currentBackGround = "backGroundGrey"
          this.mode = "Ligth Mode"
          this.boxShadow = "m-3 shadow p-3 mb-5 bg-body rounded"
          this.backgroundBody = "backgroundWhite "
          this.backgroundLogo = ""
          localStorage.setItem("mode", false)
        }

    },
    
    
    
    }
})


account.mount('#account')