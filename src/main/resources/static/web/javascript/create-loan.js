const {createApp} = Vue

const createLoan = createApp({
    data(){
        return {
            clientLoansTaken: [],
            cards:[],
            clientsAccount: {},
            
            loans: [],
            clientLoans: {},
            balanceTotalLoan: "",
            
            selectLoan: "",
            selectOwner:"asdasd",

            

            accountOwn: [],

            username: "",
            email: "",

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
            shadowCardLoan: "",
            optionBackground: "",
            buttonDisableColor: "",
            checkedHeader: true,
            header: "",
            headerDesktop: "",

            balanceTotal: "",

            view: true,

            viewPassword: "d-none",
            noViewPassword: "",


            name: "",
            payments: [],
            maxAmount: 0,
            percentIncrease: 0,
            
            
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
                this.cards = data.data.cards.sort((a,b) => a.id - b.id)
                this.username = data.data.firstName + ' ' + data.data.lastName
                this.email = data.data.email
                // this.clientsAccount = this.accountClient.sort((a,b) => a.id - b.id)
                // this.balanceTotal = this.clientsAccount.map(account => account.balance).reduce((iter, acc) => iter + acc).toFixed(2)
                this.clientLoansTaken = data.data.loans
                this.accountOwn = data.data.accountDTO.sort((a,b) => a.id - b.id)
                console.log(this.accountOwn)
                this.accountOrigin = [... data.data.accountDTO.map(number => number.number)][0]
                this.accountTarget = data.data.accountDTO.length > 1 ? data.data.accountDTO[0].number : "No other account"
                console.log(this.email)
                
            })
            .catch((error) => console.log(error))

            axios.get("/api/loans")
            .then(data => {
                this.loans = data.data
                this.loans.sort((a, b) => a.id - b.id)
                this.selectLoan = this.loans[0].name
                
                console.log(this.loans)
                console.log(this.loanQuota)
                
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
        logout() {
            axios.post('/api/logout').then(response => {
                
                window.location.href = './index.html'
            })
        },
        addCommas(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
        createLoan() {
            Swal.fire({
                title: '¿Do you Create loan?',
                showDenyButton: true,
                // showCancelButton: true,
                confirmButtonText: 'Accept',
                denyButtonText: `Cancel`,
            }).then((result) => {

                if (result.isConfirmed) {
                        axios.post('/api/loans/create', `name=${this.name}&maxAmount=${this.maxAmount}&payments=${this.payments}&percentIncrease=${this.percentIncrease}`)
                        .then(response => {
                            Swal.fire('Create Loan Success', '', 'success')
                                .then(result => {
                                    window.location.href=("./loan-application.html")
                                })
                        }).catch((error) => Swal.fire({
                            icon: "error",
                            title: "Oops...",
                            text: `${error.response.data}`,
                        }))
                } else if (result.isDenied) {
                    Swal.fire('Cancel Create Loan', '', 'error')
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
          this.shadowCardLoan = "borderCard-white"
          this.buttonDisableColor = "buttonDisableColor"
          this.optionBackground = "optionBackground-black"
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
          this.shadowCardLoan = "borderCard-black"
          this.buttonDisableColor = ""
          this.optionBackground = "optionBackground-white"
          localStorage.setItem("mode", false)
        }
    },
    
   
    
    

    
    
    
    }
})


createLoan.mount('#createLoan')
