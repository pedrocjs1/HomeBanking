const {createApp} = Vue

const account = createApp({
    data(){
        return {
            accountClient:[],
            clientsAccountActive: [],
            clientsAccount: {},
            loans: [],
            clientLoans: {},
            balanceTotalLoan: "",
            
            
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
            .get("/api/clients/current")
            .then((data) =>{
                this.accountClient = data.data.accountDTO
                this.username = data.data.firstName + ' ' + data.data.lastName
                this.clientsAccount = this.accountClient.sort((a,b) => a.id - b.id)
                this.clientsAccountActive = this.clientsAccount.filter(account => account.active === true)
                this.balanceTotal = this.clientsAccount.map(account => account.balance).reduce((iter, acc) => iter + acc).toFixed(2)
                console.log(this.accountClient)
                this.loans = data.data.loans
                this.clientLoans = this.loans.sort((a,b) => a.payment - b.payment)
                this.balanceTotalLoan = this.clientLoans.map(account => account.amount).reduce((iter, acc) => iter + acc).toFixed(2)
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
        formatedNextDate(dateInput) {
            const date = new Date(dateInput)
            date.setMonth(date.getMonth() + 1)
        
            return date.toDateString().slice(3)
        },
        formatedHour(dateInput) {
            const date = new Date(dateInput)
            let minutes = date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()
            return date.getHours() + ":" + minutes
        },
        addCommas(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
        logout() {
            axios.post('/api/logout').then(response => {
                window.location.href = './index.html'                
            })
        },
        addAccount() {
                let inputOptions = {
                    'SAVINGS': 'Savings',
                    'CHECKING': 'Checking',
                }
                Swal.fire({
                        title: 'Select account type',
                        input: 'radio',
                        inputOptions: inputOptions,
                        inputValidator: (value) => {
                            if (!value) {
                                return 'You need to choose something!'
                            }
                        }
                    })
                    .then((result) => {
                        if (result.isConfirmed) {
                            axios.post("/api/clients/current/accounts", `accountType=${result.value}`)
                                .then(response => {
                                    Swal.fire('Create Success', '', 'success')
                                        .then(result => {
                                            window.location.reload()
                                        })
                                }).catch(error => {
                                    Swal.fire('Creation Failed', error.response.data, 'error')
                                        .then(result => {
                                            window.location.reload()
                                        })
                                })
    
                        } else if (result.isDenied) {
                            Swal.fire('Cancel creation account', '', 'error')
                        }
                    })
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



