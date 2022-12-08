const {createApp} = Vue

const account = createApp({
    data(){
        return {
            accountClient:[],
            clientsAccount: {},
            
            username: "",
            

            checkedAccount: undefined,
            currentText: "",
            currentBackGround: "",
            mode: "",
            boxShadow: "",
            backgroundBody: "",
            backgroundTitle: "",
            shadowCard: "",
            bgGrey: "",
            backgroundLogo: "",

            checkedHeader: true,
            header: "",
            headerDesktop: "",

            balanceTotal: ""


            
            
            
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
          this.backgroundTitle = "bg-dark"
          this.shadowCard = "shadowWhite" 
          this.bgGrey = "bg-light borderRadius"
          this.backgroundLogo = ""
          localStorage.setItem("mode", true)

        } else if (this.checkedAccount === "false" || this.checkedAccount === false) {
          this.currentText = "text-black"
          this.currentBackGround = "backGroundGrey"
          this.mode = "Ligth Mode"
          this.boxShadow = "m-3 shadow bg-body rounded"
          this.backgroundBody = "backgroundWhite "
          this.backgroundTitle = "backgroundTitletarget"
          this.shadowCard = "shadow  bg-body rounded" 
          this.bgGrey = "backgroundBodyCard borderRadius"
          this.backgroundLogo = ""
          localStorage.setItem("mode", false)
        }
    }

    
    
    
    }
})


account.mount('#account')


